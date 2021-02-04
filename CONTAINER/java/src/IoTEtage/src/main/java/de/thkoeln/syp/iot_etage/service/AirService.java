package de.thkoeln.syp.iot_etage.service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
import de.thkoeln.syp.iot_etage.mqtt.InstructionResponseDto;
import de.thkoeln.syp.iot_etage.mqtt.MqttConfiguration.InstructionTopicGateway;

@Service
public class AirService {
  private static final Logger logger = LoggerFactory.getLogger(AirService.class);

  private final int mcuid = 1003;
  private final String sensorType = "TEMPERATURE";
  private final String action = "switchMode";

  private CountDownLatch processingLatch;
  private InstructionResponseDto instructionResponseDto;

  @Autowired
  private final AirStatus airStatus;
  @Autowired
  private final InstructionTopicGateway instructionTopicGateway;
  @Autowired
  private final EventRepository eventRepository;
  @Autowired
  private final SensorRepository sensorRepository;

  // Konstruktoren
  @Autowired
  public AirService(AirStatus airStatus, InstructionTopicGateway instructionTopicGateway,
      EventRepository eventRepository, SensorRepository sensorRepository) {
    this.airStatus = airStatus;
    this.instructionTopicGateway = instructionTopicGateway;
    this.eventRepository = eventRepository;
    this.sensorRepository = sensorRepository;
  }

  // Methoden

  /**
   * get
   */

  public AirStatusDto getCurrentState() {
    AirStatusDto airStatusDto = new AirStatusDto();

    EventData eventData = this.eventRepository.findTopByUidAndActionOrderByTimestampDesc(this.mcuid, this.action);
    if (eventData == null) {
      airStatusDto.setState(State.NO_DATA);
    } else {
      airStatusDto.setState(State.valueOf(eventData.getNewState().toUpperCase()));
    }

    SensorData sensorData = this.sensorRepository.findTopBySensorTypeOrderByTimestampDesc(this.sensorType);
    if (sensorData == null) {
      airStatusDto.setSensorValue("0");
    } else {
      airStatusDto.setSensorValue(sensorData.getPayload());
    }

    return airStatusDto;
  }

  /**
   * Status ändern. Es wird eine Instruction an Mqtt Server gesendet
   * 
   * @param instructionDto
   * @return
   */

  public InstructionResponseDto changeStatus(InstructionDto instructionDto) {

    this.instructionResponseDto = null;

    instructionDto.setMcuid(this.mcuid);

    ObjectMapper objMapper = new ObjectMapper();
    String jsonString = null;

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
    this.instructionTopicGateway.sendToMqtt(jsonString);

    try {
      this.processingLatch.await(20, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return new InstructionResponseDto(this.mcuid, false, "Interrupt Fehler auf dem Server");
    }

    if (this.instructionResponseDto == null){
      this.processingLatch = null;
      return new InstructionResponseDto(this.mcuid, false, "Keine Antwort von MCU");
    }
  
    return this.instructionResponseDto;
  }

    //Helper Funktions

    public void countProcessingLatchDown(InstructionResponseDto instResponseDto) {

      System.out.println("YES!!! InstructionResponse Recieved");
      this.instructionResponseDto = instResponseDto;
      this.processingLatch.countDown();
    }
}
