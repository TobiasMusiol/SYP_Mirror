/**************************************************************************
 This is an example for our Monochrome OLEDs based on SSD1306 drivers

 Pick one up today in the adafruit shop!
 ------> http://www.adafruit.com/category/63_98

 This example is for a 128x32 pixel display using I2C to communicate
 3 pins are required to interface (two I2C and one reset).

 Adafruit invests time and resources providing this open
 source code, please support Adafruit and open-source
 hardware by purchasing products from Adafruit!

 Written by Limor Fried/Ladyada for Adafruit Industries,
 with contributions from the open source community.
 BSD license, check license.txt for more information
 All text above, and the splash screen below must be
 included in any redistribution.
 **************************************************************************/

//DISPLAY_VARIABLE_START-------------------------------------------------------------------------------------

#include <SPI.h>
#include <Wire.h>
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>
#include "secrets.h"

#define SCREEN_WIDTH 128 // OLED display width, in pixels
#define SCREEN_HEIGHT 32 // OLED display height, in pixels
#define OLED_RESET LED_BUILTIN // Reset pin # (or -1 if sharing Arduino reset pin)
#define SCREEN_ADDRESS 0x3C ///< See datasheet for Address; 0x3D for 128x64, 0x3C for 128x32
Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, OLED_RESET);

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

//DISPLAY_VARIABLE_END-------------------------------------------------------------------------------------

//RAUMSSTAUS_VARIABLEN_START------------------------------------------------------------------------------

  typedef enum {FREI,BELEGT,REINIGEN}Raumstatus;
  Raumstatus raumstatus = FREI;
  bool lueftenNotwendig = false;

  int i = 0;
  String text;
  
//RAUMSTATUS_VARIABLEN_END--------------------------------------------------------------------------------


/***************************NETZWERK_VON_HIER****************************/

//Daten Uebertragung
#define UID 1004
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
    if(strcmp(action,"setState") == 0){
      const char* state = docIn["payload"]["state"];
      if(strcmp(state, "free") == 0) raumstatus = FREI;
      else if(strcmp(state,"occupied") == 0) raumstatus = BELEGT;
      else if(strcmp(state,"cleaning")==0) raumstatus = REINIGEN;
      Serial.print("Neuer Status: ");
      Serial.println(state);
    }
  }
}

/***************************NETZWERK_BIS_HIER****************************/


void setup() {
  Serial.begin(115200);
  if(!Serial) delay(500);
  Serial.println("\nStart");

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

  //DISPLAY_INIT_START-------------------------------------------------------------------------------------
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

  //DISPLAY_INIT_END-------------------------------------------------------------------------------------
  

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
  
  drawRaumstatus();
}

//Am Ende des Textes ist \0 Zwingend erforderlich
void drawRaumstatus(){

  if(raumstatus == FREI) text = "FREI";
  else if(raumstatus == BELEGT) text = "BELEGT";
  else if(raumstatus == REINIGEN) text = "REINIGUNG";

  display.clearDisplay();

  display.setTextSize(2);      // Normal 1:1 pixel scale
  display.setTextColor(SSD1306_WHITE); // Draw white text
  display.setCursor(0, 13);     // Start at top-left corner
  display.cp437(true);         // Use full 256 char 'Code Page 437' font

  i = 0;
  while(text[i] != '\0'){
    if (client.connected())client.loop();
    display.write(text[i]);
    i++;
  }

  display.display();
}
