package de.thkoeln.syp.iot_etage.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thkoeln.syp.iot_etage.controller.dto.AirStatusDto;
import de.thkoeln.syp.iot_etage.controller.dto.InstructionDto;
import de.thkoeln.syp.iot_etage.domain.entity.EventData;
import de.thkoeln.syp.iot_etage.domain.entity.SensorData;
import de.thkoeln.syp.iot_etage.domain.helper.State;
import de.thkoeln.syp.iot_etage.domain.model.AirStatus;
import de.thkoeln.syp.iot_etage.domain.repository.EventRepository;
import de.thkoeln.syp.iot_etage.domain.repository.SensorRepository;
import de.thkoeln.syp.iot_etage.mqtt.MqttConfiguration.InstructionTopicGateway;

@Service
public class AirService {
  private static final Logger logger = LoggerFactory.getLogger(AirService.class);

  private final int mcuid = 1003;
  private final String sensorType = "air";

  @Autowired
  private final AirStatus airStatus;
  @Autowired
  private final InstructionTopicGateway instructionTopicGateway;
  @Autowired
  private final EventRepository EventRepository;
  @Autowired
  private final SensorRepository sensorRepository;

  // Konstruktoren
  @Autowired
  public AirService(
    AirStatus airStatus,
    InstructionTopicGateway instructionTopicGateway,
    EventRepository eventRepository,
    SensorRepository sensorRepository
  ){
    this.airStatus = airStatus;
    this.instructionTopicGateway = instructionTopicGateway;
    this.EventRepository = eventRepository;
    this.sensorRepository = sensorRepository;
  }

  //Methoden

  /**
   * get 
   */
 
  public AirStatusDto getCurrentState(){
    AirStatusDto airStatusDto = new AirStatusDto();
    
    EventData eventData = this.EventRepository.findTopByTriggerOrderByTimestampDesc(this.sensorType);
    if(eventData == null){
      airStatusDto.setState(State.NO_DATA);
    }
    else {
      airStatusDto.setState(this.airStatus.getState());
    }

    SensorData sensorData = this.sensorRepository.findTopBySensorTypeOrderByTimestampDesc(this.sensorType);
    if(sensorData == null){
      airStatusDto.setSensorValue("0");
    }
    else {
      airStatusDto.setSensorValue(sensorData.getPayload());
    }

    return airStatusDto;
  }
  
  public boolean changeStatus(InstructionDto instructionDto){

    instructionDto.setMcuid(this.mcuid);

    ObjectMapper objMapper = new ObjectMapper();
    String jsonString = null;

    try {
      jsonString = objMapper.writeValueAsString(instructionDto);
    }
    catch(JsonProcessingException e){
      e.printStackTrace();
      return false;
    }

    this.instructionTopicGateway.sendToMqtt(jsonString);

    return true;
    }
}
