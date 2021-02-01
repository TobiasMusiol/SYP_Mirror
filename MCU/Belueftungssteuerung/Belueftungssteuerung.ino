#include <MQUnifiedsensor.h>
#include <SPI.h>
#include <Wire.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>
#include "secrets.h"

//Display
#define SCREEN_WIDTH 128 // OLED display width, in pixels
#define SCREEN_HEIGHT 32 // OLED display height, in pixels
#define OLED_RESET LED_BUILTIN // Reset pin # (or -1 if sharing Arduino reset pin)
#define SCREEN_ADDRESS 0x3C ///< See datasheet for Address; 0x3D for 128x64, 0x3C for 128x32
Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, OLED_RESET);
unsigned short lueftenNotwendig = 0;
int i = 0;
String text;

#define LOGO_HEIGHT   16
#define LOGO_WIDTH    16
static const unsigned char PROGMEM logo_bmp[] =
{ B00000000, B11000000,
  B00000001, B11000000,
  B00000001, B11000000,
  B00000011, B11100000,
  B11110011, B11100000,
  B11111110, B11111000,
  B01111110, B11111111,
  B00110011, B10011111,
  B00011111, B11111100,
  B00001101, B01110000,
  B00011011, B10100000,
  B00111111, B11100000,
  B00111111, B11110000,
  B01111100, B11110000,
  B01110000, B01110000,
  B00000000, B00110000 };

//MQ135 Sensor
#define placa "ESP8266"
#define Voltage_Resolution 3.3
#define pin A0 //Analog input 0 of your arduino
#define type "MQ-135" //MQ135
#define ADC_Bit_Resolution 10 // For arduino UNO/MEGA/NANO
#define RatioMQ135CleanAir 3.6//RS / R0 = 3.6 ppm  
//#define calibration_button 13 //Pin to calibrate your sensor
//Declare Sensor
MQUnifiedsensor MQ135(placa, Voltage_Resolution, ADC_Bit_Resolution, pin, type);
float CO2 = 0;
double CO2_AVG = 0;
unsigned long n = 0;

//Programmzustand
typedef enum{MANU,AUTO} Modus;
Modus modus = AUTO;

//Luefter
#define LUEFTER_PIN 15                     //Luefter Pin D3 (GPIO 0)
#define PWM_MAX 1023                       //Max Wert fuer PWM
int PWM = 0;
int PWM_PC =0;
int threshold = 100;
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
  client.publish(MQTT_PUB_DATA_TOPIC, buf, false);

  Serial.print("Published [");
  Serial.print(MQTT_PUB_DATA_TOPIC);
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
          sendResponse(action, false, "Allready in auto Mode.");
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
          sendResponse(action, false,"Allready in manuell Mode.");
        }
        else{
          modus = MANU;
          sendResponse(action, true, "");
          sendEventData(action,"AUTOMATIK","MANUELL","human");
        }
      }
      else{
        sendResponse(action, false,"Unknown Targed Mode.");
      }
    }
    
    //Threshold setzen
    else if(action == "setThreshold"){
      int threshold_in = docIn["payload"]["threshold"];
      
      if(threshold_in > 0 && threshold_in <= 10000){
        sendEventData(action,String(threshold),String(threshold_in),"human");
        threshold = threshold_in;
        sendResponse(action, true, "");
      }else{
        sendResponse(action, false, "Threshold value not in Range. Range: 1 - 10000PPM");
      }
    }
    
    //Geschwindigkeit setzten
    else if(action == "setSpeed"){
      int speed_in = docIn["payload"]["speed"];
      
      if(speed_in >= 0 && speed_in <=100){
        sendEventData(action, String(speed_pc), String(speed_in), "human");
        sendResponse(action, true, "");
        speed_pc = speed_in;
      }
      else{
        sendResponse(action, false, "Speed value not in Range. Range: 0 - 100%");
      }
    }
    else{
      sendResponse(action, false, "Unknown action.");
    }
  }
}

/***************************NETZWERK_BIS_HIER****************************/

//Am Ende des Textes ist \0 Zwingend erforderlich
void drawRaumstatus(){

  if(lueftenNotwendig == 1) text = "LUEFTEN!";
  else text = "";

  display.clearDisplay();

  display.setTextSize(2);      // Normal 1:1 pixel scale
  display.setTextColor(SSD1306_WHITE); // Draw white text
  display.setCursor(0, 13);     // Start at top-left corner
  display.cp437(true);         // Use full 256 char 'Code Page 437' font

  i = 0;
  while(text[i] != '\0'){
    display.write(text[i]);
    i++;
  }

  display.display();
}


void setup() {
  
  //Init the serial port communication - to debug the library
  Serial.begin(115200); //Init serial port
  if(!Serial) delay(500);

  /*******************************MQ135_INIT_START****************************************/  
  //Set math model to calculate the PPM concentration and the value of constants
  MQ135.setRegressionMethod(1); //_PPM =  a*ratio^b
  /*****************************  MQ Init ********************************************/ 
  //Remarks: Configure the pin of arduino as input.
  /************************************************************************************/ 
  MQ135.init(); 
  
    //If the RL value is different from 10K please assign your RL value with the following method:
    MQ135.setRL(1); //Gemessen mit Multimeter 1k
  /*****************************  MQ CAlibration ********************************************/ 
  // Explanation: 
  // In this routine the sensor will measure the resistance of the sensor supposing before was pre-heated
  // and now is on clean air (Calibration conditions), and it will setup R0 value.
  // We recomend execute this routine only on setup or on the laboratory and save on the eeprom of your arduino
  // This routine not need to execute to every restart, you can load your R0 if you know the value
  // Acknowledgements: https://jayconsystems.com/blog/understanding-a-gas-sensor
  Serial.print("Calibrating please wait.");
  float calcR0 = 0;
  for(int i = 1; i<=10; i ++)
  {
    MQ135.update(); // Update data, the arduino will be read the voltage on the analog pin
    calcR0 += MQ135.calibrate(RatioMQ135CleanAir);
    Serial.print(".");
  }
  MQ135.setR0(calcR0/10);
  Serial.println("  done!.");
  
  if(isinf(calcR0)) {Serial.println("Warning: Conection issue founded, R0 is infite (Open circuit detected) please check your wiring and supply"); while(1);}
  if(calcR0 == 0){Serial.println("Warning: Conection issue founded, R0 is zero (Analog pin with short circuit to ground) please check your wiring and supply"); while(1);}
  /*****************************  MQ CAlibration ********************************************/ 
  Serial.println("** Lectures from MQ-135 ****");
  Serial.println("|   CO2  |");  

  /*******************************MQ135_INIT_END****************************************/  
  /*******************************DISPLAY_INIT_START****************************************/
  // SSD1306_SWITCHCAPVCC = generate display voltage from 3.3V internally
  if(!display.begin(SSD1306_SWITCHCAPVCC, SCREEN_ADDRESS)) {
    Serial.println(F("SSD1306 allocation failed"));
    for(;;); // Don't proceed, loop forever
  }

  // Show initial display buffer contents on the screen --
  // the library initializes this with an Adafruit splash screen.
  display.display();
  delay(2000); // Pause for 2 seconds

  // Clear the buffer
  display.clearDisplay();

  /*******************************DISPLAY_INIT_END****************************************/


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

  MQ135.update(); // Update data, the arduino will be read the voltage on the analog pin
  MQ135.setA(110.47); MQ135.setB(-2.862); // Configurate the ecuation values to get CO2 concentration
  CO2 = MQ135.readSensor(); // Sensor will read PPM concentration using the model and a and b values setted before or in the setup
  CO2_AVG += CO2;
  n++;

  if(modus == AUTO){ 
    if(CO2 > threshold){
      analogWrite(LUEFTER_PIN,PWM_MAX);
      //Serial.println("Luefter an.");
    }else{
      analogWrite(LUEFTER_PIN,0);
      //Serial.println("Luefter aus.");
    }
  }
  //Manueller Modus
  else{
    PWM = PWM_PC*PWM_MAX/100;
    if(PWM > PWM_MAX) PWM = PWM_MAX;
    else if(PWM < 0)PWM = 0;
    analogWrite(LUEFTER_PIN,PWM); 
  }

  //Luftdaten ans Backend senden
  if(millis()-lastMillis > REFRESH_RATE_MS){
    lastMillis = millis();
    if(n!= 0)CO2_AVG = CO2_AVG / n;
    sendSensorData("AIRQUALITY",String(CO2_AVG));
    CO2_AVG = 0;
    n = 0;
  }

  //Raumluftstatus angeben
  if(CO2 > threshold){
    if(lueftenNotwendig == 0) sendEventData("checkAir","ok", "ventilate", "AIRQUALITY");
    lueftenNotwendig = 1;
  }
  else{
    if(lueftenNotwendig == 1) sendEventData("checkAir", "ventilate", "ok", "AIRQUALITY");
    lueftenNotwendig = 0;
  }
  drawRaumstatus();
}
