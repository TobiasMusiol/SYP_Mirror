package de.thkoeln.syp.iot_etage.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import de.thkoeln.syp.iot_etage.controller.dto.EventDataDto;
import de.thkoeln.syp.iot_etage.service.EventService;

public class MqttSubEventHandler implements MessageHandler {
  @Autowired
  private EventService eventService;

  public MqttSubEventHandler(){}

  @Override
  public void handleMessage(Message<?> message) throws MessagingException{
    EventDataDto newEventDataDto = null;

    System.out.println(message.getPayload().toString());

    ObjectMapper objectMapper = new ObjectMapper();
    try {
      newEventDataDto = objectMapper.readValue(message.getPayload().toString(), EventDataDto.class);
    } catch (JsonMappingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (JsonProcessingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    newEventDataDto = this.eventService.createEvent(newEventDataDto);

    System.out.println("ID: " + newEventDataDto.getId());


  }


}
