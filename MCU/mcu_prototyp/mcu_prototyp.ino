#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>
#include "DHT.h"
#include "secrets.h"

#define DHT_TYPE DHT22
#define DHT_PIN 4

#define REFRESH_RATE_MS 10000

DHT dht(DHT_PIN, DHT_TYPE);
WiFiClientSecure espClient;
PubSubClient client(espClient);
StaticJsonDocument<200> doc;

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
}

void setup()
{
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

  espClient.setTrustAnchors(&caCertX509);
  espClient.allowSelfSignedCerts();
  espClient.setFingerprint(mqttCertFingerprint);

  client.setServer(MQTT_HOST, MQTT_PORT);
  client.setCallback(receivedCallback);
  dht.begin();
  mqtt_connect();
}

void loop()
{
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

  if (millis() - lastMillis > REFRESH_RATE_MS)
  {
    lastMillis = millis();
    float h = dht.readHumidity();
    float t = dht.readTemperature();

    doc["uid"] = UID;
    doc["sensorType"] = "TEMPERATURE";
    doc["payload"] = t;

    serializeJson(doc, buf);
    client.publish(MQTT_PUB_TOPIC, buf, false);
    
    Serial.print("Published [");
    Serial.print(MQTT_PUB_TOPIC);
    Serial.print("]: ");
    Serial.println(buf);

    doc["sensorType"] = "HUMIDITY";
    doc["payload"] = h;

    serializeJson(doc, buf);
    client.publish(MQTT_PUB_TOPIC, buf, false);

    Serial.print("Published [");
    Serial.print(MQTT_PUB_TOPIC);
    Serial.print("]: ");
    Serial.println(buf);
  }
}
