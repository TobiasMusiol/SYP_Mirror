package de.thkoeln.syp.iot_etage.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import de.thkoeln.syp.iot_etage.controller.dto.SensorDataDto;
import de.thkoeln.syp.iot_etage.service.SensorService;

public class MqttSubSensorHandler implements MessageHandler {
  // Attribute
  @Autowired
  private SensorService sensorService;

  // Konstruktoren
  public MqttSubSensorHandler() {
  }

  // Methoden
  public void handleResponse(Message<?> message) {

  }

  @Override
  public void handleMessage(Message<?> message) throws MessagingException {

    SensorDataDto newSensorDataDto = null;
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      newSensorDataDto = objectMapper.readValue(message.getPayload().toString(), SensorDataDto.class);
    } catch (JsonMappingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (JsonProcessingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    newSensorDataDto = this.sensorService.insertSensorData(newSensorDataDto);
    System.out.println("Sensor ID: " + newSensorDataDto.getId());
  }
}
