#include <Stepper.h>
#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>
#include "secrets.h"

#define LIGHT_SENSOR_PIN A0
#define motSpeed 10               //Speed in RPM
#define stepsPerRev 2048          // change this to fit the number of steps per revolution
#define REVS 2                    //Anz Umdrehungen, um Markise hochzufahren

#define MPIN1 5                   //Motor PIN 1
#define MPIN2 4                   //Motor PIN 2
#define MPIN3 0                   //Motor PIN 3
#define MPIN4 2                   //Motor PIN 4

const int steps = stepsPerRev * REVS;     //Schritte fuer eine Umdrehung
Stepper myStepper(stepsPerRev, MPIN1, MPIN3, MPIN2, MPIN4);

//Programmzustand
typedef enum{MANU,AUTO} Modus;
Modus modus = MANU;

//Schalter fuer Zustandsaenderung, welche automatisch gesetzt werden
unsigned short istOben = 1;
unsigned short istUnten = 0;
unsigned short up = 0;
unsigned short down = 0;
//Eingaenge
int PWM = 0;
int PWM_PC = 0;
int threshold = 0;

//Sonsiges
int i = 0;

//------------------------------------------------------------------------

//Daten Uebertragung
#define UID 1002
#define REFRESH_RATE_MS 10000
WiFiClientSecure espClient;
PubSubClient client(espClient);
StaticJsonDocument<200> docOut;
StaticJsonDocument<200> docIn;
char buf[200];
byte mac[6];
unsigned long lastMillis = 0;

/* Certificate Authority info */
/* CA Cert in PEM format */
const char caCert[] PROGMEM = R"EOF(
-----BEGIN CERTIFICATE-----
MIIBrjCCARACFCULurSX2xF8WD+FVPHVsq+JoJ5CMAoGCCqGSM49BAMCMBYxFDAS
BgNVBAMMCzE5Mi4xNi4xLjQwMB4XDTIxMDEyMDEyMTEwM1oXDTMxMDExODEyMTEw
M1owFjEUMBIGA1UEAwwLMTkyLjE2LjEuNDAwgZswEAYHKoZIzj0CAQYFK4EEACMD
gYYABABINmZ9vNzZEg6oxo9nCVttcoB0PNjrHfPC+JIq3y9qiumuPBfc0SWXjyYp
nQnn1pBNekT/q6nQ+BE4RSrIbsjl7gBS9wOPbiKNbUpig3jisrtCxOOe3E+hn2Bc
Dw4Ci5nsYYYC0q4UMWvkZ03uIokMjiuIcHyPwrhgr6PvObnPBBCWqzAKBggqhkjO
PQQDAgOBiwAwgYcCQgE1UE6LzT/q9H0ClGliBGWVB1gEGej13dJszYwpDVL1sia8
dtzYqaQdWFDDCAwwhXffDMm1xuQ92rcjLF3s7+ZXygJBQR2Z16hpSypHQWFngrFD
Zgk70j2ZhhXE14czcHH7IuM6pid4bldDHuPBVwrxkdDL32uXCED2YqO8+ydlN7g/
aOQ=
-----END CERTIFICATE-----
)EOF";

/* MQTT broker cert SHA1 ginerprint */

const uint8_t mqttCertFingerprint[] = {0xA9, 0x01, 0x05, 0x2E, 0x5F, 0x52, 0x55, 0x6A, 0x17, 0xDA, 0x55, 0x34, 0x04, 0x5A, 0xD7, 0xA9, 0x7B, 0xE3, 0x75, 0x59};

X509List caCertX509(caCert);

void mqtt_connect()
{
  while (!client.connected())
  {
    Serial.print("MQTT connecting ... ");
    if (client.connect(HOSTNAME, MQTT_USER, MQTT_PASS))
    {
      Serial.println("connected.");
      client.subscribe(MQTT_SUB_TOPIC);
    } else {
      Serial.print("failed, status code =");
      Serial.print(client.state());
      Serial.println(". Try again in 5 seconds.");
      delay(5000);
    }
  }
}

void receivedCallback(char* topic, byte* payload, unsigned int length)
{
  Serial.print("Received [");
  Serial.print(topic);
  Serial.print("]: ");
  for (int i = 0; i < length; i++)
  {
    Serial.print((char)payload[i]);
  }
  Serial.println("");

  const char* payloadBuffer = (const char*) payload;
  deserializeJson(docIn, payloadBuffer);
  
  int uid = docIn["MCUID"];
  const char* action = docIn["action"];
  
  Serial.println(uid);
  Serial.println(action);
  
  if(uid == UID){
    if(strcmp(action,"switchMode") == 0){
      const char* targetMode = docIn["payload"]["targetMode"];
      if(strcmp(targetMode,"auto") == 0){
        int threshold_in = docIn["payload"]["threshold"];
        if(threshold_in < 0) threshold = 0;
        else if(threshold_in > 100) threshold = 0;
        else threshold = threshold_in;
        Serial.println(targetMode);
        Serial.println(threshold);
        modus = AUTO;
        Serial.print("Neuer Threshold(Prozentual): ");
        Serial.println(threshold);
      }
      else if(strcmp(targetMode,"manu") == 0){
        modus = MANU;
      }
      Serial.print("Modus ist jetzt: ");
      Serial.println(modus);
    }
    else if(strcmp(action,"setThreshold") == 0){
      int threshold_in = docIn["payload"]["threshold"];
      if(threshold_in < 0) threshold = 0;
      else if(threshold_in > 100) threshold = 0;
      else threshold = threshold_in;
      Serial.print("Neuer Threshold(Prozentual): ");
      Serial.println(threshold);
    }
    else if(strcmp(action,"toggle") == 0 && modus == MANU){
    const char* direction_in = docIn["payload"]["direction"];
    Serial.println(direction_in);
    if(( strcmp(direction_in,"up") == 0) && (istUnten == 1) ){
      up = 1;
      down = 0;
      Serial.println("Runterfahren.");
    }
    else if( (strcmp(direction_in,"down") == 0) && (istOben == 1) ){
      up = 0;
      down = 1;
      Serial.println("Runterfahren.");
    }
  }
  }
}

//------------------------------------------------------------------------

void setup() {
  // set the speed at 60 rpm:
  myStepper.setSpeed(motSpeed);
  // initialize the serial port:
  Serial.begin(115200);
  if(!Serial) delay(500);

  /***************************NETZWERK_VON_HIER****************************/
  Serial.print("Attempting to connect to SSID: ");
  Serial.print(SECRET_SSID);
  WiFi.hostname(HOSTNAME);
  WiFi.mode(WIFI_STA);
  WiFi.begin(SECRET_SSID, SECRET_PASS);
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(1000);
  }
  Serial.println("connected!");
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());
  WiFi.macAddress(mac);
  Serial.print("MAC: ");
  Serial.print(mac[5],HEX);
  Serial.print(":");
  Serial.print(mac[4],HEX);
  Serial.print(":");
  Serial.print(mac[3],HEX);
  Serial.print(":");
  Serial.print(mac[2],HEX);
  Serial.print(":");
  Serial.print(mac[1],HEX);
  Serial.print(":");
  Serial.println(mac[0],HEX);

  espClient.setTrustAnchors(&caCertX509);
  espClient.allowSelfSignedCerts();
  espClient.setFingerprint(mqttCertFingerprint);

  client.setServer(MQTT_HOST, MQTT_PORT);
  client.setCallback(receivedCallback);
  mqtt_connect();
  /***************************NETZWERK_BIS_HIER****************************/  
}       

void motor_abschalten(){
  digitalWrite(MPIN1, LOW);
  digitalWrite(MPIN2, LOW);
  digitalWrite(MPIN3, LOW);
  digitalWrite(MPIN4, LOW);
}

//Drives the motor revolution Revolutions 
//Feeds the Watchdog
//Markise hoch
void fahre_hoch(){
  for(i = 0; i < steps; i++){
    if (client.connected()) client.loop();
    ESP.wdtFeed();
    myStepper.step(-1);
    //Serial.println(i);
  }
  motor_abschalten();
  istOben = 1;
  istUnten = 0;
}

//Drives the motor revolution Revolutions
//Feeds the Watchdog
//Markise runter
void fahre_runter(){
  for(i = 0; i < steps; i++){
    if (client.connected()) client.loop();
    ESP.wdtFeed();
    myStepper.step(1);
    //Serial.println(i);
  }
  motor_abschalten();
  istOben = 0;
  istUnten = 1;
}



void loop() {

  /***************************NETZWERK_VON_HIER****************************/
  //Verbindung aufbauen, falls keine Verbindung besteht.
  if (WiFi.status() != WL_CONNECTED)
  {
    Serial.print("Checking wifi");
    while (WiFi.waitForConnectResult() != WL_CONNECTED)
    {
      WiFi.begin(SECRET_SSID, SECRET_PASS);
      Serial.print(".");
      delay(10);
    }
    Serial.println("connected");
  } else {
    if (!client.connected())
    {
      mqtt_connect();
    } else {
      client.loop();
    }
  }
  /***************************NETZWERK_BIS_HIER****************************/

  if(modus == AUTO){
    if(millis()-lastMillis > REFRESH_RATE_MS){
        lastMillis = millis();
      //Eingang auslesen
      //Licht gering => R hoch => U hoch => PWM hoch
      PWM = analogRead(LIGHT_SENSOR_PIN);
      PWM_PC = PWM*100/1023;
      if(PWM_PC <= threshold && istOben == 1){
        fahre_runter();
      }else if(PWM_PC > threshold && istUnten == 1){
        fahre_hoch();
      }else{
        motor_abschalten();
      }
  
      //Sensordaten hochladen
        docOut["uid"] = UID;
        docOut["sensorType"] = "LIGHT_OUTSIDE";
        docOut["payload"] = PWM_PC;
    
        serializeJson(docOut, buf);
        client.publish(MQTT_PUB_TOPIC, buf, false);
        
        Serial.print("Published [");
        Serial.print(MQTT_PUB_TOPIC);
        Serial.print("]: ");
        Serial.println(buf);
    }
    
          
  }else{
    if(down == 1){
      fahre_runter();
      down = 0;
    }
    else if(up == 1){
      fahre_hoch();
      up = 0;
    }
    else{
      motor_abschalten();
    }
  }
  

}
