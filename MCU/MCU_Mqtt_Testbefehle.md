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
    Startet im Automatik Modus

        Zu Manuellen Modus wechseln:
        mosquitto_pub -h localhost -p 1883 -u admin -P admin -t /iot_etage/instructions --message "{\"MCUID\": 1001,\"action\": \"switchMode\",\"payload\": {\"targetMode\": \"manu\"}}"

        Zu Automatik Modus wechseln:
        mosquitto_pub -h localhost -p 1883 -u admin -P admin -t /iot_etage/instructions --message "{'MCUID': 1001,'action': 'switchMode','payload': {'targetMode': 'auto'}}"

        PWM Wert einstellen:
        mosquitto_pub -h localhost -p 1883 -u admin -P admin -t /iot_etage/instructions --message "{\"MCUID\": 1001,\"action\": \"setBrigthness\",\"payload\": {\"brigthness\": 60}}"

        Variablen erklärt:
            targetMode:
                auto : Automatik Modus
                manu : Manueller Modus

            brigthness:
                0 bis 100 :  Prozentualler Wert der Lichtstärke
            
            


    Markisensteuerung:
    Startet im Manellen Modus

        Zu Automatik Modus wechseln und Threshold einstellen:
        mosquitto_pub -h localhost -p 1883 -u admin -P admin -t /iot_etage/instructions --message "{\"MCUID\": 1002,\"action\": \"switchMode\",\"payload\": {\"targetMode\": \"manu\",\"threshold\": 60}}"

        Zu Manuellen Modus wechseln:
        mosquitto_pub -h localhost -p 1883 -u admin -P admin -t /iot_etage/instructions --message "{\"MCUID\": 1002,\"action\": \"switchMode\",\"payload\": {\"targetMode\": \"manu\"}}"

        Threshold einstellen:
        mosquitto_pub -h localhost -p 1883 -u admin -P admin -t /iot_etage/instructions --message "{\"MCUID\": 1002,\"action\":\"setThreshold\",\"payload\": {\"threshold\": 60}}"

        Runter Fahren:
        mosquitto_pub -h localhost -p 1883 -u admin -P admin -t /iot_etage/instructions --message "{\"MCUID\": 1002,\"action\":\"toggle\",\"payload\":{\"direction\" : \"down\"}}"

        Hoch Fahren:
        mosquitto_pub -h localhost -p 1883 -u admin -P admin -t /iot_etage/instructions --message "{\"MCUID\": 1002,\"action\":\"toggle\",\"payload\":{\"direction\" : \"up\"}}"

        Variablen erklärt:
            targetMode:
                auto : Automatik Modus
                manu : Manueller Modus

            threshold:
                0 bis 100 : Schwellwet der angibt, ab wie viel Prozent des PWM inputs 
                            des Lichtsensors die Markise hoch oder runter fährt. 

            direction:
                up : Markise hochfahren
                down : Markise herunterfahren

