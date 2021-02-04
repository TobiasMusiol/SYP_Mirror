Starten von Mosquitto in der Docker Shell:

    docker exec -it mosquitto sh

Zm testen muss der Port 1883 in der "mosquitto.conf" definiert sein: 
listener 1883

Im Microcrontroller muss der Port 8884 benutzt werden.

Antworten auf die versendeten Befehle kann man im entsprechendem Topic empfangen.

SENSORDATEN MITHÖREN:

    mosquitto_sub -h localhost -p 1883 -u admin -P admin -t /iot_etage/sensordata

EVENTDATA MITHÖREN:

    mosquitto_sub -h localhost -p 1883 -u admin -P admin -t /iot_etage/eventdata

BEFEHLE MITHÖREN:

    mosquitto_sub -h localhost -p 1883 -u admin -P admin -t /iot_etage/instructions

ANTWORTEN MITHÖREN

    mosquitto_sub -h localhost -p 1883 -u admin -P admin -t /iot_etage/instructions/response

RESPONSE SENSEN:

    mosquitto_pub -h localhost -p 1883 -u admin -P admin -t /iot_etage/instructions --message "{\"UID\":1003,\"action\":\"switchMode\",\"success\":\"true\", \"message\":\"\"}"

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

        Zu Automatik Modus wechseln:
        mosquitto_pub -h localhost -p 1883 -u admin -P admin -t /iot_etage/instructions --message "{\"MCUID\": 1002,\"action\": \"switchMode\",\"payload\": {\"targetMode\": \"auto\"}}"

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
                0 bis 100 : Schwellwert der angibt, ab wie viel Prozent des PWM inputs 
                            des Lichtsensors die Markise hoch oder runter fährt. 

            direction:
                up : Markise hochfahren
                down : Markise herunterfahren


    Belüftungssteuerung:
    Startet im manuellen Modus

        Zu Automatik Modus wechseln:
        mosquitto_pub -h localhost -p 1883 -u admin -P admin -t /iot_etage/instructions --message "{\"MCUID\": 1003,\"action\": \"switchMode\",\"payload\": {\"targetMode\": \"auto\"}}"

        Zu Manuellen Modus wechseln:
        mosquitto_pub -h localhost -p 1883 -u admin -P admin -t /iot_etage/instructions --message "{\"MCUID\": 1003,\"action\": \"switchMode\",\"payload\": {\"targetMode\": \"manu\"}}"

        Lüftergeschwindigkeit einstellen:
        mosquitto_pub -h localhost -p 1883 -u admin -P admin -t /iot_etage/instructions --message "{\"MCUID\": 1003,\"action\":\"setSpeed\",\"payload\": {\"speed\": 100}}"

        Threshold einstellen:
        mosquitto_pub -h localhost -p 1883 -u admin -P admin -t /iot_etage/instructions --message "{\"MCUID\": 1003,\"action\":\"setThreshold\",\"payload\": {\"threshold\": 20}}"

        Variablen erklärt:

            targetMode:
                auto : Automatik Modus
                manu : Manueller Modus

            threshold:
                Ein Temperatur Wert zwischen 0 und  50°C : Sinnvoller Temperaturwert. Range habe ich von 0°C bis 100C° eingestellt. Andere werte spucken eine Fehlermeldung aus. 

            speed:
                0 bis 100 : Stellt die Geschwindigkeit des Lüfter auf einen Prozentuallen Wert von 0 bis 100

    Raumstatus:
    Startet mit dem Status 'free'

        Setzte Status free:
        mosquitto_pub -h localhost -p 1883 -u admin -P admin -t /iot_etage/instructions --message "{\"MCUID\": 1004, \"action\": \"setState\",\"payload\": {\"state\": \"free\"}}"

        Setze Status occupied:
        mosquitto_pub -h localhost -p 1883 -u admin -P admin -t /iot_etage/instructions --message "{\"MCUID\": 1004, \"action\": \"setState\",\"payload\": {\"state\": \"occupied\"}}"

        Setze Status cleaning:
        mosquitto_pub -h localhost -p 1883 -u admin -P admin -t /iot_etage/instructions --message "{\"MCUID\": 1004, \"action\": \"setState\",\"payload\": {\"state\": \"cleaning\"}}"

        Variablen erklärt:

            state:
                free : Der Raum ist Frei und kann genutzt werden.
                occupied : Der Raum ist belegt und kann nicht genutzt werden.
                cleaning : Der Raum befindet sich gerade in der Reinigung. (Ähnlich wie im Kino)

    Raumluftstatus:
    Gibt an, ob die Luft im Raum in Ordnung ist.

        Threshold einstellen:
        mosquitto_pub -h localhost -p 1883 -u admin -P admin -t /iot_etage/instructions --message "{\"MCUID\": 1005,\"action\":\"setThreshold\",\"payload\": {\"threshold\": 20}}"


            
Mogliche Werte für Sensortype:

    HUMIDITY_RELATIVE
    TEMPERATURE
    AIRPRESSURE
    AIRQUALITY
    LIGHTLEVEL_OUTDOOR
    LIGHTLEVEL_INDOOR
