package de.thkoeln.syp.iot_etage.service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import de.thkoeln.syp.iot_etage.auth.UserPrincipal;
import de.thkoeln.syp.iot_etage.auth.helper.AppRole;
import de.thkoeln.syp.iot_etage.controller.dto.InstructionDto;
import de.thkoeln.syp.iot_etage.controller.dto.RoomStatusDto;
import de.thkoeln.syp.iot_etage.domain.entity.EventData;
import de.thkoeln.syp.iot_etage.domain.helper.RoomModus;
import de.thkoeln.syp.iot_etage.domain.model.RoomStatus;
import de.thkoeln.syp.iot_etage.domain.repository.EventRepository;
import de.thkoeln.syp.iot_etage.mqtt.InstructionResponseDto;
import de.thkoeln.syp.iot_etage.mqtt.MqttConfiguration.InstructionTopicGateway;

@Service
public class RoomStatusService {
  
  private final int mcuid = 1003;
  private final String sensorType = "roomstatus";
  private CountDownLatch processingLatch;
  private InstructionResponseDto instructionResponseDto;

  @Autowired
  private RoomStatus roomStatus;
  @Autowired
  private final InstructionTopicGateway instructionTopicGateway;
  @Autowired
  private final EventRepository eventRepository;


  // Constructoren
  public RoomStatusService(
    InstructionTopicGateway instructionTopicGateway,
    EventRepository eventRepository 
  ){
    this.instructionTopicGateway = instructionTopicGateway;
    this.eventRepository = eventRepository;
  }

  //Methoden
  public RoomStatusDto getCurrentRoomStatus(){
    RoomStatusDto roomStatusDto = new RoomStatusDto();

    EventData eventData = this.eventRepository.findTopByTriggerOrderByTimestampDesc(this.sensorType);
    if (eventData == null) {
      roomStatusDto.setRoomModus(RoomModus.NO_DATA);
    } else {
      roomStatusDto.setRoomModus(this.roomStatus.getRoomModus());
    }

    return roomStatusDto;
  }

  public InstructionResponseDto changeRoomStatus(InstructionDto instrDto)throws Exception{

    boolean allow = false;

    //UserPrincipal
    SecurityContext context = SecurityContextHolder.getContext();
    Authentication auth = context.getAuthentication();
    UserPrincipal userPrincial = (UserPrincipal) auth.getPrincipal();

    if (
      (instrDto.getAction() == "setState" && instrDto.getPayload().get("payload") == "frei")
      &&
      (userPrincial.getUser().getAppRole() == AppRole.ADMIN || userPrincial.getUser().getAppRole() == AppRole.FACILITY_MANAGER)
    ){
      allow = true;
    }
    else if(
      instrDto.getAction() == "setThreshold"
      &&
      (userPrincial.getUser().getAppRole() == AppRole.ADMIN || userPrincial.getUser().getAppRole() == AppRole.FACILITY_MANAGER)
    ){
      allow = true;
    }

    if(allow){

      this.instructionResponseDto = null;
      
      instrDto.setMcuid(this.mcuid);
    
      ObjectMapper objMapper = new ObjectMapper();
      String jsonString = null;

      try {
        jsonString = objMapper.writeValueAsString(instrDto);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
        return this.instructionResponseDto;
      }

      this.processingLatch = new CountDownLatch(1);
      this.instructionTopicGateway.sendToMqtt(jsonString);

      try {
        this.processingLatch.await(20, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return this.instructionResponseDto;
      }

      this.roomStatus.setRoomModus((RoomModus)instrDto.getPayload().get("state"));

      if(instrDto.getAction() == "setState"){
        this.roomStatus.setRoomModus((RoomModus) instrDto.getPayload().get("state"));
      }

      return this.instructionResponseDto;
    }

    return null;
  }

  //Helper Funktions

  public void countProcessingLatchDown(InstructionResponseDto instResDto) {

    System.out.println("YES!!! InstructionResponse Recieved");
    this.processingLatch.countDown();
    this.processingLatch = null;
    this.instructionResponseDto = instResDto;
  }
}
