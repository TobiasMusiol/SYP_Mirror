package de.thkoeln.syp.iot_etage.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thkoeln.syp.iot_etage.MqttConfiguration.MyGateway;
import de.thkoeln.syp.iot_etage.domain.model.AirStatus;
import de.thkoeln.syp.iot_etage.domain.repository.EventRepository;

@Service
public class AirService {
  private static final Logger logger = LoggerFactory.getLogger(AirService.class);

  private final EventRepository EventRepository;
  private final AirStatus airStatus;
  private final MyGateway myGateway;

  // Konstruktoren
  @Autowired
  public AirService(
    EventRepository eventRepository,
    AirStatus airStatus,
    MyGateway myGateway
  ){
    this.EventRepository = eventRepository;
    this.airStatus = airStatus;
    this.myGateway = myGateway;
  }

  /**
   * get latest 
   */
 
  public void testMqtt(){
    this.myGateway.sendToMqtt("Haha Test");
  }
  

  
}
