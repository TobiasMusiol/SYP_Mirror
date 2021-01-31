package de.thkoeln.syp.iot_etage.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thkoeln.syp.iot_etage.controller.dto.InstructionDto;
import de.thkoeln.syp.iot_etage.controller.dto.LightStatusDto;
import de.thkoeln.syp.iot_etage.domain.entity.EventData;
import de.thkoeln.syp.iot_etage.domain.entity.SensorData;
import de.thkoeln.syp.iot_etage.domain.helper.State;
import de.thkoeln.syp.iot_etage.domain.model.LightStatus;
import de.thkoeln.syp.iot_etage.domain.repository.EventRepository;
import de.thkoeln.syp.iot_etage.domain.repository.SensorRepository;
import de.thkoeln.syp.iot_etage.mqtt.MqttConfiguration.InstructionTopicGateway;

/**
 * Service für Beleuchtungssteueurng
 */
@Service
public class LightService {

  private static final Logger logger = LoggerFactory.getLogger(LightService.class);

  private final int mcuid = 1001;
  private final String sensorType = "LIGHT_INSIDE";

  @Autowired
  private LightStatus lightStatus;
  @Autowired
  private InstructionTopicGateway instructionTopicGateways;
  @Autowired
  private EventRepository eventRepository;
  @Autowired
  private SensorRepository sensorRepository;


  //Konstruktoren
  public LightService() {}


  // Methods
  public LightStatusDto getStatus(){
    LightStatusDto lightStatusDto = new LightStatusDto();

    EventData eventData = this.eventRepository.findTopByTriggerOrderByTimestampDesc(this.sensorType);
    if (eventData == null){
      lightStatusDto.setState(State.NO_DATA);
    }
    else{
      lightStatusDto.setState(this.lightStatus.getState());
    }

    SensorData sensorData = this.sensorRepository.findTopBySensorTypeOrderByTimestampDesc(this.sensorType); 
    if(sensorData == null){
      lightStatusDto.setSensorValue("0");
    }
    else {
      lightStatusDto.setSensorValue(sensorData.getPayload());
    }

    return lightStatusDto;
  }  

  public boolean changeStatus(InstructionDto instructionDto) {

    instructionDto.setMcuid(this.mcuid);

    ObjectMapper objMapper = new ObjectMapper();
    String jsonString;

    try {
      jsonString = objMapper.writeValueAsString(instructionDto);
    } catch (JsonProcessingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return false;
    }

    this.instructionTopicGateways.sendToMqtt(jsonString);
    
    return true;
  }
}