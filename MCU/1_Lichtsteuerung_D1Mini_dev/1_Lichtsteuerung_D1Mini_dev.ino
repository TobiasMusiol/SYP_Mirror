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
int PWM_OUT = 0;
int PWM_OUT_PC = 0;
int PWM_SENSOR = 0;
int PWM_SENSOR_PC = 0;

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


void sendSensorData(String sensorType, String payload){
  char buf[200];
  docOut["UID"] = UID;
  docOut["sensorType"] = sensorType;
  docOut["payload"] = payload;
      
  serializeJson(docOut, buf);
  client.publish(MQTT_PUB_DATA_TOPIC, buf, false);
            
  Serial.print("Published [");
  Serial.print(MQTT_PUB_DATA_TOPIC);
  Serial.print("]: ");
  Serial.println(buf);
}

void sendEventData(String action, String oldState, String newState, String trigger){
  char buf[200];
  StaticJsonDocument<200> docEvent;
  docEvent["UID"] = UID;
  docEvent["action"] = action;
  docEvent["oldState"] = oldState;
  docEvent["newState"] = newState;
  docEvent["trigger"] = trigger;
  serializeJson(docEvent, buf);
  client.publish(MQTT_PUB_EVENT_TOPIC, buf, false);

  Serial.print("Published [");
  Serial.print(MQTT_PUB_EVENT_TOPIC);
  Serial.print("]: ");
  Serial.println(buf);
}

void sendResponse(String action, bool success, String message){
  char buf[200];
  StaticJsonDocument<200> docResponse;
  docResponse["UID"] = String(UID);
  docResponse["action"] = action;
  docResponse["success"] = success;
  docResponse["message"] = message;

  serializeJson(docResponse, buf);
  client.publish(MQTT_PUB_RESPONSE_TOPIC, buf, false);

  Serial.print("Published [");
  Serial.print(MQTT_PUB_RESPONSE_TOPIC);
  Serial.print("]: ");
  Serial.println(buf);
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
  String action = docIn["action"];
  
  Serial.println(uid);
  Serial.println(action);
  
  if(uid == UID){
    
    if(action == "switchMode"){
      String targetMode = docIn["payload"]["targetMode"];
      String oldState;
      
      if(modus == AUTO) oldState = "auto";
      else oldState = "manuell";
      
      if(targetMode == "auto"){
        
        if(modus != AUTO){
          modus = AUTO;
          sendEventData(action, oldState, "auto", "human");
          sendResponse(action, true, "");
        }
        else{
          sendResponse(action, false, "Bereits im Automatik Modus.");
        }
      }
      else if(targetMode == "manu"){
        
        if(modus != MANU){
          modus = MANU;
          sendEventData(action, oldState, "manuell", "human");
          sendResponse(action, true, "");
        }
        else{
          sendResponse(action, false, "Bereits im manuellen Modus.");
        }
        
      }
      
    }
    else if(action == "setBrigthness"){

      if(modus == MANU){

        int PWM_IN_PC = docIn["payload"]["brigthness"];
        
        if(PWM_IN_PC >= 0 && PWM_IN_PC <= 100){
          sendEventData(action, String(PWM_OUT_PC), String(PWM_IN_PC), "human");
          sendResponse(action, true, "");
          PWM_OUT_PC = PWM_IN_PC;
        }
        else{
          sendResponse(action, false, "Lichtwert im Wertebereich (0 - 100%)");
        }
        
      }
      else{
        sendResponse(action, false, "Steuerung ist nicht im manuellen Modus.");
      }
      
    }
    else{
      sendResponse(action, false, "Unbekannte Aktion.");
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
  //Aktuellen Zustand senden
  String modeString;
  if(modus == AUTO) modeString = "AUTOMATIK";
  else modeString = "MANUEL";
  sendEventData("INITIAL_MODUS","",modeString,HOSTNAME");
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

  PWM_SENSOR = analogRead(LIGHT_SENSOR_PIN); //PWM Wert einlesen
  delay(100);
  PWM_SENSOR = 1023 - PWM_SENSOR; 
  PWM_SENSOR_PC = PWM_SENSOR*100/1023;
  
  if (millis() - lastMillis > REFRESH_RATE_MS){
    lastMillis = millis();
    sendSensorData("LIGHTLEVEL_INDOOR",String(PWM_SENSOR_PC));
  }   
  
  //Manueller Modus
  //MCU erhält PWM_PC Wert und muss ihn hier nur ausfuehren
  if(modus == MANU){
    PWM_OUT = PWM_OUT_PC*PWM_MAX/100;
    if(PWM_OUT > PWM_MAX) PWM_OUT = PWM_MAX;
    else if(PWM_OUT < 0)PWM_OUT = 0;
    analogWrite(LIGHT_PIN,PWM_OUT);   
  }
  
  //Automatik Modus
  else{
    PWM_OUT = 1023 
    - PWM_SENSOR;
    if(PWM_OUT > PWM_MAX) PWM_OUT = PWM_MAX;   //Begrenzung, damit die LED Kette nicht zu Warm wird
    else if(PWM_OUT < 0) PWM_OUT = 0;
    PWM_OUT_PC = PWM_OUT*100/PWM_MAX;         //PWM Wert in Prozent umrechnen
    analogWrite(LIGHT_PIN,PWM_OUT);
  }
  
}
