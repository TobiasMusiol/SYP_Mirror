package de.thkoeln.syp.iot_etage.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thkoeln.syp.iot_etage.controller.dto.AwningStatusDto;
import de.thkoeln.syp.iot_etage.controller.dto.InstructionDto;
import de.thkoeln.syp.iot_etage.domain.entity.EventData;
import de.thkoeln.syp.iot_etage.domain.entity.SensorData;
import de.thkoeln.syp.iot_etage.domain.helper.State;
import de.thkoeln.syp.iot_etage.domain.model.AwningStatus;
import de.thkoeln.syp.iot_etage.domain.model.LightStatus;
import de.thkoeln.syp.iot_etage.domain.repository.EventRepository;
import de.thkoeln.syp.iot_etage.domain.repository.SensorRepository;
import de.thkoeln.syp.iot_etage.mqtt.MqttConfiguration.InstructionTopicGateway;

/**
* Service für Markisolettensteuerung  
*/

@Service
public class AwningService {
  
  private static final Logger logger = LoggerFactory.getLogger(AwningService.class);

  private final int mcuid = 1002;
  private final String sendorType = "LIGHT_OUTSIDE";

  private final AwningStatus awningStatus;
  private final InstructionTopicGateway instrTopicGateway;
  private final EventRepository eventRepo;
  private final SensorRepository sensorRepo;


  @Autowired
  public AwningService(
    AwningStatus awningStatus,
    InstructionTopicGateway instrTopicGateway,
    EventRepository eventRepo,
    SensorRepository sensorRepo
  ){
    this.awningStatus = awningStatus;
    this.instrTopicGateway = instrTopicGateway;
    this.eventRepo = eventRepo;
    this.sensorRepo = sensorRepo;
  }

  /**
  * aktuellen Zustand lesen 
  */

  public AwningStatusDto getCurrentState(){
    AwningStatusDto awningStatusDto = new AwningStatusDto();

    EventData eventData = this.eventRepo.findTopByTriggerOrderByTimestampDesc(this.sendorType);
    if(eventData == null){
      awningStatusDto.setState(State.NO_DATA);
    }
    else {
      awningStatusDto.setState(this.awningStatus.getState());
    }

    SensorData sensorData = this.sensorRepo.findTopBySensorTypeOrderByTimestampDesc(this.sendorType);
    if(sensorData == null){
      awningStatusDto.setSensorValue("0");
    }
    else {
      awningStatusDto.setSensorValue(sensorData.getPayload());
    }

    return awningStatusDto;
  }

  /**
  * aktuellen Zustand ändern 
  */
  public boolean changeState(InstructionDto instrDto){

    instrDto.setMcuid(this.mcuid);

    ObjectMapper objMapper = new ObjectMapper();
    String jsonString;

    try {
      jsonString = objMapper.writeValueAsString(instrDto);
    } catch (JsonProcessingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return false;
    }

    this.instrTopicGateway.sendToMqtt(jsonString);
    
    return true;
  }
}
