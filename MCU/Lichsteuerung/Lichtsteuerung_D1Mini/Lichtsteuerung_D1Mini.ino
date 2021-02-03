#include <Arduino.h>
#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>
#include "DHT.h"
#include "secrets.h"

//IO-PIN
#define LIGHT_PIN 16
#define LIGHT_SENSOR_PIN A0

#define REFRESH_RATE_MS 10000

//Variablen
typdef enum {AUTO, MANU}Modus;
Modus modus = AUTO;
DHT dht(DHT_PIN, DHT_TYPE);
WiFiClient espClient;
PubSubClient client(espClient);
StaticJsonDocument<200> doc;
int i = 0;
int PWM = 0;
int PWM_PC =0;
char buf[200];
byte mac[6];
unsigned long lastMillis = 0;

//------------------------------------------------------------------------

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
}

//------------------------------------------------------------------------


void setup() {
  pinMode(LIGHT_PIN, OUTPUT);
  Serial.begin(115200);
  if(!Serial) delay(500);
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

  client.setServer(MQTT_HOST, MQTT_PORT);
  client.setCallback(receivedCallback);
  dht.begin();
  mqtt_connect();
}

//------------------------------------------------------------------------

void loop() {

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
  
  //Automatik Modus
  if(modus == AUTO){
    PWM = analogRead(LIGHT_SENSOR_PIN); //PWM Wert einlesen
    PWM_PC = PWM*100/1023;              //PWM Wert in Prozent umrechnen
    if(PWM > 600) PWM = 600;            //Begrenzung, damit die LED Kette nicht zu Warm wird
    else if(PWM < 50 ) PWM = 0;
    analogWrite(LIGHT_PIN,PWM);         

    doc["uid"] = UID;
    doc["sensorType"] = "LIGHT";
    doc["payload"] = PWM_PC;

    serializeJson(doc, buf);
    client.publish(MQTT_PUB_TOPIC, buf, false);
    
    Serial.print("Published [");
    Serial.print(MQTT_PUB_TOPIC);
    Serial.print("]: ");
    Serial.println(buf);
  }
  //Manueller Modus
  //MCU erhält PWM Wert und muss ihn hier nur ausfuehren
  else{
    if(PWM > 600) PWM = 600;            //Begrenzung, damit die LED Kette nicht zu Warm wird
    else if(PWM < 50 ) PWM = 0;
    analogWrite(LIGHT_PIN,PWM);
  }
}
