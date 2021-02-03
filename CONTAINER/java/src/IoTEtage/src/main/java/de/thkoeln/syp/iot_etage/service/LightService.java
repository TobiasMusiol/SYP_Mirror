package de.thkoeln.syp.iot_etage.service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
import de.thkoeln.syp.iot_etage.mqtt.InstructionResponseDto;
import de.thkoeln.syp.iot_etage.mqtt.MqttConfiguration.InstructionTopicGateway;

/**
 * Service für Beleuchtungssteueurng
 */
@Service
public class LightService {

  private static final Logger logger = LoggerFactory.getLogger(LightService.class);

  private final int mcuid = 1001;
  private final String sensorType = "LIGHTLEVEL_INDOOR";
  private final String action="switchMode";
  private CountDownLatch processingLatch;
  private InstructionResponseDto instructionResponse;

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

    EventData eventData = this.eventRepository.findTopByUidAndActionOrderByTimestampDesc(this.mcuid, this.action);
    if (eventData == null){
      lightStatusDto.setState(State.NO_DATA);
    }
    else{
      lightStatusDto.setState(State.valueOf( eventData.getNewState().toUpperCase()));
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

  /**
   * 
   * @param instructionDto
   * @return
   */
  public InstructionResponseDto changeStatus(InstructionDto instructionDto) {

    this.instructionResponse = null;

    instructionDto.setMcuid(this.mcuid);

    ObjectMapper objMapper = new ObjectMapper();
    String jsonString;

    try {
      jsonString = objMapper.writeValueAsString(instructionDto);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return new InstructionResponseDto(this.mcuid, false, "Falsches Instruction Format");
    }


    if(this.processingLatch != null && this.processingLatch.getCount() > 0){
      return new InstructionResponseDto(this.mcuid, false, "Andere Instruction wird ausgeführt");
    }

    this.processingLatch = new CountDownLatch(1);
    this.instructionTopicGateways.sendToMqtt(jsonString);

    try {
      this.processingLatch.await(20, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return new InstructionResponseDto(this.mcuid, false, "Interrupt Fehler auf dem Server");
    }

    if (this.instructionResponse == null){
      return new InstructionResponseDto(this.mcuid, false, "Keine Antwort von MCU");
    }

    return this.instructionResponse;
  }

  //Helper Funktions

  public void countProcessingLatchDown(InstructionResponseDto instrRes) {

    System.out.println("YES!!! InstructionResponse Recieved");
    this.instructionResponse = instrRes;
    this.processingLatch.countDown();
  }
}