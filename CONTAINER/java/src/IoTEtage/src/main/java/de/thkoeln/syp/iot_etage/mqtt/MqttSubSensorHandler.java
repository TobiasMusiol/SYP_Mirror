package de.thkoeln.syp.iot_etage.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import de.thkoeln.syp.iot_etage.controller.dto.SensorDataDto;
import de.thkoeln.syp.iot_etage.domain.Settings;
import de.thkoeln.syp.iot_etage.service.AirService;
import de.thkoeln.syp.iot_etage.service.AwningService;
import de.thkoeln.syp.iot_etage.service.LightService;
import de.thkoeln.syp.iot_etage.service.RoomStatusService;
import de.thkoeln.syp.iot_etage.service.SensorService;

public class MqttSubSensorHandler implements MessageHandler {
  // Attribute
  @Autowired
  private Settings settings;

  @Autowired
  private SensorService sensorService;
  
  @Autowired
  private AirService airService;
  @Autowired
  private AwningService awningService;
  @Autowired
  private LightService lightService;
  @Autowired
  private RoomStatusService roomStatusService;
  
  // Konstruktoren
  public MqttSubSensorHandler() {
  }

  // Methoden

  @Override
  public void handleMessage(Message<?> message) throws MessagingException {

    SensorDataDto newSensorDataDto = null;
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      newSensorDataDto = objectMapper.readValue(message.getPayload().toString(), SensorDataDto.class);
    } catch (JsonMappingException e) {
      e.printStackTrace();
      return;
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return;
    }
    newSensorDataDto = this.sensorService.insertSensorData(newSensorDataDto);
    System.out.println("Sensor ID: " + newSensorDataDto.getId());

    if (this.settings.getSentToTB()){
      switch ((int) newSensorDataDto.getUid()) {
        case 1001:
        this.lightService.sentToThingsboard(newSensorDataDto.getPayload()); 
        break;
        case 1002:
        this.awningService.sentToThingsboard(newSensorDataDto.getPayload());
        break;
        case 1003:
        this.airService.sentToThingsboard(newSensorDataDto);
        break;
        case 1005:
        this.roomStatusService.sentToThingsboard(newSensorDataDto.getPayload());
        break;
        default:
        break;
      }
    }
  }
}
