#include <SPI.h>
#include <Wire.h>
#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>
#include <Adafruit_Sensor.h>
#include <Adafruit_BME280.h>
#include "secrets.h"


//BME280
float TEMP = 0;
int TEMP_INT = 0;
float HUMY_REL = 0;
float PRES = 0;
char* temp;
char* humy;
char* pres;
int x = 0;
#define SEALEVELPRESSURE_HPA (1013.25)
Adafruit_BME280 bme; // I2C


//Programmzustand
typedef enum{MANU,AUTO} Modus;
Modus modus = AUTO;

//Luefter
#define LUEFTER_PIN D5                    //Luefter Pin D5 
#define PWM_MAX 1023                       //Max Wert fuer PWM
int PWM = 0;
int PWM_PC =0;
int threshold = 30;
int speed_pc = 0;


/***************************NETZWERK_VON_HIER****************************/

//Daten Uebertragung
#define UID 1003
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

void sendSensorData(String sensorType, float payload){
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

void sendResponse(String action,bool success, String message){
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
  String response = "";
  
  Serial.print("Received [");
  Serial.print(topic);
  Serial.print("]: ");
  for (int i = 0; i < length; i++)
  {
    Serial.print((char)payload[i]);
  }
  Serial.println("");

  //Nachricht parsen
  const char* payloadBuffer = (const char*) payload;
  deserializeJson(docIn, payloadBuffer);
  
  int uid = docIn["MCUID"];
  String action = docIn["action"];
 
  if(uid == UID){
    //Modus wechslen
    if(action =="switchMode"){
      String targetMode = docIn["payload"]["targetMode"];
      
      //Automatik
      if(targetMode == "auto"){
        
        if(modus == AUTO){
          sendResponse(action, false, "Bereits im automatik Modus.");
        }
        else{
          modus = AUTO;
          sendResponse(action, true, "");
          sendEventData(action,"MANUELL","AUTOMATIK","human");
        } 
      }
      //Manuell
      else if(targetMode == "manu"){
        
        if(modus == MANU){
          sendResponse(action, false,"Bereits im manuellen Modus.");
        }
        else{
          modus = MANU;
          sendResponse(action, true, "");
          sendEventData(action,"AUTOMATIK","MANUELL","human");
        }
      }
      else{
        sendResponse(action, false,"Unbekannter Zielmodus.");
      }
    }
    
    //Threshold setzen
    else if(action == "setThreshold"){
      int threshold_in = docIn["payload"]["threshold"];
      
      if(threshold_in >= 0 && threshold_in <= 100){
        sendEventData(action,String(threshold),String(threshold_in),"human");
        threshold = threshold_in;
        sendResponse(action, true, "");
      }else{
        sendResponse(action, false, "Threshold nicht im Wertebereich. (0-100°C)");
      }
    }
    
    //Geschwindigkeit setzten
    else if(action == "setSpeed"){
      int speed_in = docIn["payload"]["speed"];
      if(modus == MANU){
        
        if(speed_in >= 0 && speed_in <=100){
          
          if(modus == MANU){
            sendEventData(action, String(speed_pc), String(speed_in), "human");
            sendResponse(action, true, "");
            speed_pc = speed_in;
          }
          else{
            sendResponse(action, false, "Geschwindigkeit einstellen nur im manuellen Modus möglich.");
          }
          
        }
        else{
          sendResponse(action, false, "Geschwindigkeitsvariable nicht im Wertebereich. (0 - 100%)");
        }
        
      }
      else{
        sendResponse(action, false, "Geschwindigkeit einstellen nur im manuellen Modus möglich.");
      }
      
    }
    else{
      sendResponse(action, false, "Unbekannte Aktion.");
    }
  }
}

/***************************NETZWERK_BIS_HIER****************************/


void setup() {
  
  //Init the serial port communication - to debug the library
  Serial.begin(115200); //Init serial port
  if(!Serial) delay(500);


  /*******************************BME180_INIT_START****************************************/  
  Serial.println(F("BME280 test"));
    unsigned status;
    
    // default settings
    status = bme.begin(0x76);  
    // You can also pass in a Wire library object like &Wire2
    //status = bme.begin(0x76, &Wire2)
    if (!status) {
        Serial.println("Could not find a valid BME280 sensor, check wiring, address, sensor ID!");
        Serial.print("SensorID was: 0x"); Serial.println(bme.sensorID(),16);
        Serial.print("        ID of 0xFF probably means a bad address, a BMP 180 or BMP 085\n");
        Serial.print("   ID of 0x56-0x58 represents a BMP 280,\n");
        Serial.print("        ID of 0x60 represents a BME 280.\n");
        Serial.print("        ID of 0x61 represents a BME 680.\n");
        while (1) delay(10);
    }

  /*******************************BME180_INIT_END****************************************/

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
  sendEventData("INITIA_MODUS","",modeString,HOSTNAME);
}

void loop() {

  /***************************NETZWERK_VON_HIER****************************/
  //Verbindung aufbauen, falls keine Verbindung besteht.
  if (WiFi.status() != WL_CONNECTED){
    Serial.print("Checking wifi");
    while (WiFi.waitForConnectResult() != WL_CONNECTED){
      WiFi.begin(SECRET_SSID, SECRET_PASS);
      Serial.print(".");
      delay(10);
    }
    Serial.println("connected");
  } 
  else{
    if(!client.connected()){
      mqtt_connect();
    } 
    else{
      client.loop();
    }
  }
  /***************************NETZWERK_BIS_HIER****************************/

  //Luftdaten ans Backend senden
  if(millis()-lastMillis > REFRESH_RATE_MS){
    lastMillis = millis();

    TEMP = bme.readTemperature();
    TEMP_INT = TEMP;
    PRES = bme.readPressure()/100.0F;
    HUMY_REL = bme.readHumidity();
    
    //printValues();
    sendSensorData("TEMPERATURE",TEMP);
    sendSensorData("HUMIDITY_RELATIVE",HUMY_REL);
    sendSensorData("AIRPRESSURE",PRES);
  }

  if(modus == AUTO){ 
    if(TEMP_INT > threshold){
      analogWrite(LUEFTER_PIN,PWM_MAX);
      //Serial.println("Luefter an.");
    }else{
      analogWrite(LUEFTER_PIN,0);
      //Serial.println("Luefter aus.");
    }
  }
  //Manueller Modus
  else{
    PWM = speed_pc*PWM_MAX/100;
    if(PWM > PWM_MAX) PWM = PWM_MAX;
    else if(PWM < 0)PWM = 0;
    analogWrite(LUEFTER_PIN,PWM); 
  }

}



void printValues() {
    Serial.print("Temperature = ");
    Serial.print(bme.readTemperature());
    Serial.println(" *C");

    Serial.print("Pressure = ");

    Serial.print(bme.readPressure() / 100.0F);
    Serial.println(" hPa");

    Serial.print("Approx. Altitude = ");
    Serial.print(bme.readAltitude(SEALEVELPRESSURE_HPA));
    Serial.println(" m");

    Serial.print("Humidity = ");
    Serial.print(bme.readHumidity());
    Serial.println(" %");

    Serial.println();
}
