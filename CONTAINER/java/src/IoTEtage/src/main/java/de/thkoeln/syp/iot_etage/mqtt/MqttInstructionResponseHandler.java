package de.thkoeln.syp.iot_etage.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import de.thkoeln.syp.iot_etage.domain.model.RoomStatus;
import de.thkoeln.syp.iot_etage.service.AirService;
import de.thkoeln.syp.iot_etage.service.AwningService;
import de.thkoeln.syp.iot_etage.service.LightService;

public class MqttInstructionResponseHandler implements MessageHandler {

  @Autowired
  private AirService airService;
  @Autowired 
  private AwningService awningService;
  @Autowired
  private LightService lightService;
  @Autowired 
  private RoomStatus roomStatusService;

  @Override
  public void handleMessage(Message<?> message) throws MessagingException {
    InstructionResponseDto newInstructionResponseDto = null;

    System.out.println(message.getPayload().toString());

    ObjectMapper objectMapper = new ObjectMapper();

    try {
      newInstructionResponseDto = objectMapper.readValue(message.getPayload().toString(), InstructionResponseDto.class);
    } catch (JsonMappingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (JsonProcessingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    System.out.println("InstructionResposne recieved");
    switch (newInstructionResponseDto.getMCUID()){
      case this.ai
    }
    this.airService.countProcessingLatchDown();
  }
}
