package de.thkoeln.syp.iot_etage.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import de.thkoeln.syp.iot_etage.service.AirService;
import de.thkoeln.syp.iot_etage.service.AwningService;
import de.thkoeln.syp.iot_etage.service.LightService;
import de.thkoeln.syp.iot_etage.service.RoomStatusService;

public class MqttInstructionResponseHandler implements MessageHandler {

  @Autowired
  private AirService airService;
  @Autowired 
  private AwningService awningService;
  @Autowired
  private LightService lightService;
  @Autowired 
  private RoomStatusService roomStatusService;

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
      // Beleuchung
      case 1001:
        this.lightService.countProcessingLatchDown(newInstructionResponseDto);
        break;
      // Markisen
      case 1002:
        this.awningService.countProcessingLatchDown(newInstructionResponseDto);
        break;
      //Lüftung
      case 1003:
        this.airService.countProcessingLatchDown(newInstructionResponseDto);
        break;
      //Roomstatus
      case 1004:
        this.roomStatusService.countProcessingLatchDown(newInstructionResponseDto);
        break;
      default:
        break;
    }
  }
}
