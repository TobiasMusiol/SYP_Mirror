#define SECRET_SSID "<YOUR SSID>"
#define SECRET_PASS "<YOUR SSID PASSWORD>"

//MCU1001 = Lichtsteuerung
//MCU1002 = Markisensteuerung
//MCU1003 = Belueftungssteuerung
//MCU1004 = Raumstatus
#define HOSTNAME "MCU1004"

#define MQTT_HOST "<MQTT BROKER ADDRESS>"
#define MQTT_PORT 8884
#define MQTT_USER "<MQTT USERNAME>"
#define MQTT_PASS "<MQTT PASSWORD>"

#define MQTT_SUB_TOPIC "/iot_etage/instructions"
#define MQTT_PUB_DATA_TOPIC "/iot_etage/sensordata"
#define MQTT_PUB_RESPONSE_TOPIC "/iot_etage/instructions/response"
#define MQTT_PUB_EVENT_TOPIC "/iot_etage/eventdata"