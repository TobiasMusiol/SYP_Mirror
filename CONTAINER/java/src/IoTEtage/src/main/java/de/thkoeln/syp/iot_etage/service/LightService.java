package de.thkoeln.syp.iot_etage.service;

import java.util.Random;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thkoeln.syp.iot_etage.controller.dto.InstructionDto;
import de.thkoeln.syp.iot_etage.controller.dto.LightStatusDto;
import de.thkoeln.syp.iot_etage.domain.helper.State;
import de.thkoeln.syp.iot_etage.domain.model.LightStatus;
import de.thkoeln.syp.iot_etage.domain.repository.EventRepository;
import de.thkoeln.syp.iot_etage.domain.repository.SensorRepository;
import de.thkoeln.syp.iot_etage.mqtt.MqttConfiguration.InstructionTopicGateway;
import de.thkoeln.syp.iot_etage.utils.LightStatusMapper;

/**
 * Service für Beleuchtungssteueurng
 */
@Service
public class LightService {

  private static final Logger logger = LoggerFactory.getLogger(LightService.class);

  private final int mcuid = 1001;
  private final String sensorType = "Light";

  @Autowired
  private LightStatus lightStatus;
  @Autowired
  private InstructionTopicGateway instructionTopicGateways;
  @Autowired
  private EventRepository eventRepository;
  @Autowired
  private SensorRepository sensorRepository;

  public LightService() {}

  // Methods
  /**
   * den aktuellen Zustand lesen
   */
  public LightStatusDto getCurrentLightState() {

    return LightStatusMapper.convertLightStatusToLightStatusDto(this.lightStatus);
  }

  /**
   * Beleuchtungssteuerung Zustand initialisieren -> wird nur vom MC ausgerufen
   */
  public LightStatusDto initLightState(LightStatusDto lightStatusDto) {

    return LightStatusMapper.convertLightStatusToLightStatusDto(this.lightStatus);
  }

  public boolean changeStatus(InstructionDto instructionDto) {

    boolean sent = false;
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
    sent = true;
    
    return sent;
  }
}