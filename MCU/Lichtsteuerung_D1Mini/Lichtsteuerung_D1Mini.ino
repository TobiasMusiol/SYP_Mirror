#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>
#include "secrets.h"

//IO-PIN
#define LIGHT_PIN 16
#define LIGHT_SENSOR_PIN A0

//Modus
typedef enum {AUTO, MANU}Modus;
Modus modus = AUTO;

//Lichtstaerke
#define PWM_MAX 600                       //Max Wert fuer PWM
int PWM = 0;
int PWM_PC =0;

//Sonstige
int i = 0;

/***************************NETZWERK_VON_HIER****************************/

//Daten Uebertragung
#define UID 1001
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
    if(strcmp(action,"switchMode")==0){
      const char* targetMode = docIn["payload"]["targetMode"];
      Serial.println(targetMode);
      if(strcmp(targetMode,"auto")==0) modus = AUTO;
      else if(strcmp(targetMode,"manu")==0) modus = MANU;
      Serial.print("Modus ist jetzt: ");
      Serial.println(modus);
    }
    else if(strcmp(action,"setBrigthness")==0 && modus == MANU){
      PWM_PC = docIn["payload"]["brigthness"];
      if(PWM_PC > 100) PWM_PC = 100;
      else if(PWM_PC < 0) PWM_PC = 0;
      Serial.print("Neuer PWM Wert(Prozentual): ");
      Serial.println(PWM_PC);
    }
  }
  
}

/***************************NETZWERK_BIS_HIER****************************/

void setup() {
  pinMode(LIGHT_PIN, OUTPUT);
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
  //Manueller Modus
  //MCU erhält PWM_PC Wert und muss ihn hier nur ausfuehren
  if(modus == MANU){
    PWM = PWM_PC*PWM_MAX/100;
    if(PWM > PWM_MAX) PWM = PWM_MAX;
    else if(PWM < 0)PWM = 0;
    analogWrite(LIGHT_PIN,PWM);   
  }
  //Automatik Modus
  else{
    if (millis() - lastMillis > REFRESH_RATE_MS){
      lastMillis = millis();
      
      PWM = analogRead(LIGHT_SENSOR_PIN); //PWM Wert einlesen      
      if(PWM > PWM_MAX) PWM = PWM_MAX;   //Begrenzung, damit die LED Kette nicht zu Warm wird
      else if(PWM < 0) PWM = 0;
      PWM_PC = PWM*100/PWM_MAX;         //PWM Wert in Prozent umrechnen  
      analogWrite(LIGHT_PIN,PWM);         
  
      //Sensordaten hochladen
      docOut["uid"] = UID;
      docOut["sensorType"] = "LIGHT_INSIDE";
      docOut["payload"] = PWM_PC;
  
      serializeJson(docOut, buf);
      client.publish(MQTT_PUB_TOPIC, buf, false);
      
      Serial.print("Published [");
      Serial.print(MQTT_PUB_TOPIC);
      Serial.print("]: ");
      Serial.println(buf);
    }
    
  }
}
