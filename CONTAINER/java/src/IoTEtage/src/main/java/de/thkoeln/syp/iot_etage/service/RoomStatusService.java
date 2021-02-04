package de.thkoeln.syp.iot_etage.service;

import java.util.Map;
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
  
  private final int mcuid = 1004;
  private final String sensorType = "roomstatus";
  private final String action = "setState";
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

    EventData eventData = this.eventRepository.findTopByUidAndActionOrderByTimestampDesc(this.mcuid, this.action);
    if (eventData == null) {
      roomStatusDto.setRoomModus(RoomModus.NO_DATA);
    } else {
      roomStatusDto.setRoomModus(RoomModus.valueOf(eventData.getNewState().toUpperCase()));
    }

    return roomStatusDto;
  }

  public InstructionResponseDto changeRoomStatus(InstructionDto instrDto){

    if(!this.checkPermissions(instrDto)){
      return new InstructionResponseDto(this.mcuid, false, "Operation ist nicht erlaubt");
    }

    this.instructionResponseDto = null;
    
    instrDto.setMcuid(this.mcuid);
  
    ObjectMapper objMapper = new ObjectMapper();
    String jsonString = null;

    try {
      jsonString = objMapper.writeValueAsString(instrDto);
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

  public void countProcessingLatchDown(InstructionResponseDto instResDto) {

    System.out.println("YES!!! InstructionResponse Recieved");
    this.instructionResponseDto = instResDto;
    this.processingLatch.countDown();
  }

  public boolean checkPermissions(InstructionDto instrDto){
    boolean allowed = false;

    //UserPrincipal
    SecurityContext context = SecurityContextHolder.getContext();
    Authentication auth = context.getAuthentication();
    UserPrincipal userPrincial = (UserPrincipal) auth.getPrincipal();

    //DebuG
    String test1 = instrDto.getAction();
    Object test2 = instrDto.getPayload().get("state");

    //Debug Ende 

    if (
      (instrDto.getAction().equals("setState") && instrDto.getPayload().get("state").equals("frei"))
      &&
      (userPrincial.getUser().getAppRole() == AppRole.ADMIN || userPrincial.getUser().getAppRole() == AppRole.FACILITY_MANAGER)
    ){
      allowed = true;
    }

    else if (instrDto.getAction().equals("setState") && !instrDto.getPayload().get("state").equals("frei")){
        allowed = true;
    }
    else if(
      instrDto.getAction() == "setThreshold"
      &&
      (userPrincial.getUser().getAppRole() == AppRole.ADMIN || userPrincial.getUser().getAppRole() == AppRole.FACILITY_MANAGER)
    ){
      allowed = true;
    }
    return allowed;
  }
}
