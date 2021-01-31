Starten von Mosquitto in der Docker Shell:

    docker exec -it mosquitto sh

Zm testen muss der Port 1883 in der "mosquitto.conf" definiert sein: 
listener 1883

SENSORDATEN MITHÖREN:

    mosquitto_sub -h localhost -p 1883 -u admin -P admin -t /iot_etage/sensordata

BEFEHLE MITHÖREN:

    mosquitto_sub -h localhost -p 1883 -u admin -P admin -t /iot_etage/instructions


BEFEHLE SENDEN:

    Lichtsteuerung:

        Modus wechseln:
        mosquitto_pub -h localhost -p 1883 -u admin -P admin -t /iot_etage/instructions --message "{\"MCUID\": 1001,\"action\": \"switchMode\",\"payload\": {\"targetMode\": \"manu\"}}"

        Automatik Modus:
        mosquitto_pub -h localhost -p 1883 -u admin -P admin -t /iot_etage/instructions --message "{'MCUID': 1001,'action': 'switchMode','payload': {'targetMode': 'auto'}}"

        PWM Wert einstellen:
        mosquitto_pub -h localhost -p 1883 -u admin -P admin -t /iot_etage/instructions --message "{\"MCUID\": 1001,\"action\": \"setBrigthness\",\"payload\": {\"brigthness\": 60}}"


    Markisensteuerung:

        Modus wechseln:
        mosquitto_pub -h localhost -p 1883 -u admin -P admin -t /iot_etage/instructions --message "{\"MCUID\": 1002,\"action\": \"switchMode\",\"payload\": {\"targetMode\": \"manu\",\"threshold\": 60}}"

        Threshold einstellen:
        mosquitto_pub -h localhost -p 1883 -u admin -P admin -t /iot_etage/instructions --message "{\"MCUID\": 1002,\"action\":\"setThreshold\",\"payload\": {\"threshold\": 60}}"

        Hoch/Runter Fahren:
        mosquitto_pub -h localhost -p 1883 -u admin -P admin -t /iot_etage/instructions --message "{\"MCUID\": 1002,\"action\":\"toggle\",\"payload\":{\"direction\" : \"down\"}}"

