package de.thkoeln.syp.iot_etage.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import de.thkoeln.syp.iot_etage.auth.UserPrincipal;
import de.thkoeln.syp.iot_etage.auth.helper.AppRole;
import de.thkoeln.syp.iot_etage.controller.dto.InstructionDto;
import de.thkoeln.syp.iot_etage.controller.dto.RoomStatusDto;
import de.thkoeln.syp.iot_etage.domain.entity.EventData;
import de.thkoeln.syp.iot_etage.domain.entity.SensorData;
import de.thkoeln.syp.iot_etage.domain.helper.RoomModus;
import de.thkoeln.syp.iot_etage.domain.repository.EventRepository;
import de.thkoeln.syp.iot_etage.domain.repository.SensorRepository;
import de.thkoeln.syp.iot_etage.mqtt.InstructionResponseDto;
import de.thkoeln.syp.iot_etage.mqtt.MqttConfiguration.InstructionTopicGateway;

@Service
public class RoomStatusService {

  private static final Logger logger = LoggerFactory.getLogger(LightService.class);
  
  private final int[] mcuids = {1004, 1005};
  private final String SensorType = "AIRQUALITY";
  private final String[] actions = {"setState", "checkAir"};

  @Value("${thingsboard.telemetryurl.mcu1005}")
  private String url;

  private CountDownLatch processingLatch;
  private InstructionResponseDto instructionResponseDto;

  @Autowired
  private final InstructionTopicGateway instructionTopicGateway;
  @Autowired
  private final EventRepository eventRepository;
  @Autowired
  private final SensorRepository sensorRepository;


  // Constructoren
  public RoomStatusService(
    InstructionTopicGateway instructionTopicGateway,
    EventRepository eventRepository,
    SensorRepository sensorRepository
  ){
    this.instructionTopicGateway = instructionTopicGateway;
    this.eventRepository = eventRepository;
    this.sensorRepository = sensorRepository;
  }

  //Methoden
  public RoomStatusDto getCurrentRoomStatus(){
    RoomStatusDto roomStatusDto = new RoomStatusDto();

    for (int i = 0; i < this.mcuids.length; i++) {
      
      EventData eventData = this.eventRepository.findTopByUidAndActionOrderByTimestampDesc(this.mcuids[i], this.actions[i]);
      if (eventData == null) {
        switch (this.mcuids[i]){
          case 1004:
            roomStatusDto.setRoomModus(RoomModus.NO_DATA); 
            break;
          case 1005:
            roomStatusDto.setAirQuality("NO DATA");
            break;
          default:
            break;
        }
      } else {
        switch (eventData.getUid()){
          case 1004:
            roomStatusDto.setRoomModus(RoomModus.valueOf(eventData.getNewState().toUpperCase()));
          case 1005:
            roomStatusDto.setAirQuality(eventData.getNewState());
        }
      }
    }

    SensorData sensorData = this.sensorRepository.findTopBySensorTypeOrderByTimestampDesc(this.SensorType);

    if(sensorData == null){
      roomStatusDto.setSensorValue("0");
    }
    else{
      roomStatusDto.setSensorValue(sensorData.getPayload());
    }

    return roomStatusDto;
  }

  public InstructionResponseDto changeRoomStatus(InstructionDto instrDto){

    if(!this.checkPermissions(instrDto)){
      return new InstructionResponseDto(this.mcuids[0], false, "Operation ist nicht erlaubt");
    }

    this.instructionResponseDto = null;
    
    instrDto.setMcuid(this.mcuids[0]);
  
    ObjectMapper objMapper = new ObjectMapper();
    String jsonString = null;

    try {
      jsonString = objMapper.writeValueAsString(instrDto);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return new InstructionResponseDto(this.mcuids[0], false, "Falsches Instruction Format");
    }

    if(this.processingLatch != null && this.processingLatch.getCount() > 0){
      return new InstructionResponseDto(this.mcuids[0], false, "Andere Instruction wird ausgeführt");
    }

    this.processingLatch = new CountDownLatch(1);
    this.instructionTopicGateway.sendToMqtt(jsonString);

    try {
      this.processingLatch.await(20, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return new InstructionResponseDto(this.mcuids[0], false, "Interrupt Fehler auf dem Server");
    }

    if (this.instructionResponseDto == null){
      this.processingLatch = null;
      return new InstructionResponseDto(this.mcuids[0], false, "Keine Antwort von MCU");
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

  public void sentToThingsboard(String payload){

    Map<String, Double> thingsBoardMessage = new HashMap<>();

    thingsBoardMessage.put("airquality", Double.valueOf(payload));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    
    HttpEntity<Map<String, Double>> httpBody = new HttpEntity<>(thingsBoardMessage);

    RestTemplate request = new RestTemplate();
    
    ResponseEntity<Object> response = null;

    try{
      response = request.postForEntity(this.url, httpBody, Object.class);
    }
    catch(RestClientException e){
       //e.printStackTrace();
       this.logger.error("Error beim Senden an Thingsboard: " + e.getMessage());
       return;
    }
      
    if (response.getStatusCode() == HttpStatus.OK){
      System.out.println("Alles Gut");
    }
  }
}
