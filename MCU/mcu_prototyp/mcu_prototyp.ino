#include <ESP8266WiFi.h>
#include <PubSubClient.h>

const char ssid[] = "UPC555B3BD";
const char pass[] = "Kcuhkku27zxu";

#define HOSTNAME "SENSOR_PROTOTYPE"

const char MQTT_HOST[] = "192.168.0.97";
const int MQTT_PORT = 1884;
const char MQTT_USER[] = "admin";
const char MQTT_PASS[] = "admin";

const char MQTT_SUB_TOPIC[] = "/test";
const char MQTT_PUB_TOPIC[] = "/test";

WiFiClient espClient;
PubSubClient client(espClient);

unsigned long lastMillis = 0;

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
  Serial.println("");-
}

void setup()
{
  Serial.begin(115200);
  Serial.println();
  Serial.println();
  Serial.print("Attempting to connect to SSID: ");
  Serial.print(ssid);
  WiFi.hostname(HOSTNAME);
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, pass);
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(1000);
  }
  Serial.println("connected!");
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());

  client.setServer(MQTT_HOST, MQTT_PORT);
  client.setCallback(receivedCallback);
  mqtt_connect();
}

void loop()
{
  if (WiFi.status() != WL_CONNECTED)
  {
    Serial.print("Checking wifi");
    while (WiFi.waitForConnectResult() != WL_CONNECTED)
    {
      WiFi.begin(ssid, pass);
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

  if (millis() - lastMillis > 5000)
  {
    lastMillis = millis();
    client.publish(MQTT_PUB_TOPIC, "DEBUG MESSAGE", false);
  }
}
