package de.thkoeln.syp.iot_etage.service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
import de.thkoeln.syp.iot_etage.mqtt.InstructionResponseDto;
import de.thkoeln.syp.iot_etage.mqtt.MqttConfiguration.InstructionTopicGateway;

/**
* Service für Markisolettensteuerung  
*/

@Service
public class AwningService {
  
  private static final Logger logger = LoggerFactory.getLogger(AwningService.class);

  private final int mcuid = 1002;
  private final String sendorType = "LIGHTLEVEL_OUTDOOR";
  private final String action="switchMode";

  private CountDownLatch processingLatch;
  private InstructionResponseDto instRes;

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
    this.processingLatch = null;
  }

  /**
  * aktuellen Zustand lesen 
  */

  public AwningStatusDto getCurrentState(){
    AwningStatusDto awningStatusDto = new AwningStatusDto();

    EventData eventData = this.eventRepo.findTopByUidAndActionOrderByTimestampDesc(this.mcuid, this.action);
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
  public InstructionResponseDto changeState(InstructionDto instrDto){

    this.instRes = null;

    instrDto.setMcuid(this.mcuid);

    ObjectMapper objMapper = new ObjectMapper();
    String jsonString;

    try {
      jsonString = objMapper.writeValueAsString(instrDto);
    } catch (JsonProcessingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return new InstructionResponseDto(this.mcuid, false, "Falsches Instruction Format");

    }

    if(this.processingLatch != null && this.processingLatch.getCount() > 0){
      return new InstructionResponseDto(this.mcuid, false, "Andere Instruction wird ausgeführt");
    }

    this.processingLatch = new CountDownLatch(1);
    this.instrTopicGateway.sendToMqtt(jsonString);

    try {
      this.processingLatch.await(20, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return new InstructionResponseDto(this.mcuid, false, "Keine Antwort von MCU");
    }

    try {
      this.processingLatch.await(20, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return new InstructionResponseDto(this.mcuid, false, "Interrupt Fehler auf dem Server");
    }

    if (this.instRes == null){
      this.processingLatch = null;
      return new InstructionResponseDto(this.mcuid, false, "Keine Antwort von MCU");
    }

    return this.instRes;
  }

  public void countProcessingLatchDown(InstructionResponseDto instRes) {

    System.out.println("YES!!! InstructionResponse Recieved");
    this.instRes = instRes;
    this.processingLatch.countDown();
  }
}
