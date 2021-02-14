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
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import de.thkoeln.syp.iot_etage.controller.dto.AirStatusDto;
import de.thkoeln.syp.iot_etage.controller.dto.InstructionDto;
import de.thkoeln.syp.iot_etage.controller.dto.SensorDataDto;
import de.thkoeln.syp.iot_etage.domain.entity.EventData;
import de.thkoeln.syp.iot_etage.domain.entity.SensorData;
import de.thkoeln.syp.iot_etage.domain.helper.State;
import de.thkoeln.syp.iot_etage.domain.repository.EventRepository;
import de.thkoeln.syp.iot_etage.domain.repository.SensorRepository;
import de.thkoeln.syp.iot_etage.mqtt.InstructionResponseDto;
import de.thkoeln.syp.iot_etage.mqtt.MqttConfiguration.InstructionTopicGateway;

@Service
public class AirService {
  private static final Logger logger = LoggerFactory.getLogger(AirService.class);
  
  @Value("${thingsboard.telemetryurl.mcu1003}")
  private String url;

  private final int mcuid = 1003;
  private final String[] sensorTypes = {"TEMPERATURE", "HUMIDITY_RELATIVE",  "AIRPRESSURE"};

  private final String action = "switchMode";

  private CountDownLatch processingLatch;
  private InstructionResponseDto instructionResponseDto;

  @Autowired
  private final InstructionTopicGateway instructionTopicGateway;
  @Autowired
  private final EventRepository eventRepository;
  @Autowired
  private final SensorRepository sensorRepository;

  // Konstruktoren
  @Autowired
  public AirService(InstructionTopicGateway instructionTopicGateway,
      EventRepository eventRepository, SensorRepository sensorRepository) {
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
    
    for (String sensorType : sensorTypes) {
      
      SensorData sensorData = this.sensorRepository.
      findTopBySensorTypeOrderByTimestampDesc(sensorType);

      if (sensorData == null) {
        switch (sensorType) {
          case "TEMPERATURE":
            airStatusDto.setSensorValueTemp("0");   
            break;
          case "HUMIDITY_RELATIVE":
            airStatusDto.setSensorValueHumidity("0");
            break;
          case "AIRPRESSURE":
            airStatusDto.setSensorValueAirPressure("0");
            break;
        }  
      } else {
        String payload = sensorData.getPayload();
        switch (sensorType) {
          case "TEMPERATURE":
            airStatusDto.setSensorValueTemp(payload);  
            break;
          case "HUMIDITY_RELATIVE":
            airStatusDto.setSensorValueHumidity(payload);
            break;
          case "AIRPRESSURE":
            airStatusDto.setSensorValueAirPressure(payload);
            break;
        }
      }
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

    public void sentToThingsboard(SensorDataDto sensorData){

      String payload = sensorData.getPayload();

      Map<String, Double> thingsBoardMessage = new HashMap<>();
      
      if(sensorData.getSensorType().equals("TEMPERATURE")){
        thingsBoardMessage.put("temperature", Double.valueOf(payload));
      }
      else if(sensorData.getSensorType().equals("HUMIDITY_RELATIVE")){
        thingsBoardMessage.put("humidity", Double.valueOf(payload));
      }
      else if(sensorData.getSensorType().equals("AIRPRESSURE")){
        thingsBoardMessage.put("airpressure", Double.valueOf(payload));
      }
      else {
        return;
      }
  
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
        this.logger.error("Error beim Senden an Thingsboard: " +  e.getMessage());
        return;
      }
        
      if (response.getStatusCode() == HttpStatus.OK){
        System.out.println("Alles Gut");
      }
    }
}
