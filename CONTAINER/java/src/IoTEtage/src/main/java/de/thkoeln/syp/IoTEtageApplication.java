package de.thkoeln.syp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.MessageChannel;

import de.thkoeln.syp.iot_etage.MqttConfiguration;

@SpringBootApplication
public class IoTEtageApplication {

    public static void main(String[] args) {
        SpringApplication.run(IoTEtageApplication.class, args);
        
    }

}
