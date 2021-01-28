package de.thkoeln.syp.iot_etage.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thkoeln.syp.iot_etage.domain.model.AirStatus;
import de.thkoeln.syp.iot_etage.domain.repository.EventRepository;
import de.thkoeln.syp.iot_etage.mqtt.MqttConfiguration.InstructionTopicGateway;

@Service
public class AirService {
  private static final Logger logger = LoggerFactory.getLogger(AirService.class);

  private final EventRepository EventRepository;
  private final AirStatus airStatus;
  private final InstructionTopicGateway instructionTopicGateway;

  // Konstruktoren
  @Autowired
  public AirService(
    EventRepository eventRepository,
    AirStatus airStatus,
    InstructionTopicGateway instructionTopicGateway
  ){
    this.EventRepository = eventRepository;
    this.airStatus = airStatus;
    this.instructionTopicGateway = instructionTopicGateway;
  }

  /**
   * get latest 
   */
 
  public void testMqtt(){
    
    this.instructionTopicGateway.sendToMqtt("Haha Test");
  }
  

  
}
