package de.thkoeln.syp.iot_etage.controller;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.thkoeln.syp.iot_etage.auth.helper.AppAuthority;
import de.thkoeln.syp.iot_etage.controller.dto.InstructionDto;
import de.thkoeln.syp.iot_etage.controller.dto.LightStatusDto;
import de.thkoeln.syp.iot_etage.controller.dto.ResponseDto;
import de.thkoeln.syp.iot_etage.mqtt.MqttConfiguration.InstructionTopicGateway;
import de.thkoeln.syp.iot_etage.service.LightService;

/**
 * Beleuchtungssteuerung Rest Controller
 */
// @Validated
@RestController
@RequestMapping(path = "/light")
public class LightController {

  private final int mcuid = 1001;
  private final String sensorType = "Light";

  @Autowired
  private LightService lightService;

  // public LightController(
  // LightService lightService,
  // InstructionTopicGateway instructionTopicGateway){
  // this.lightService = lightService;
  // this.instructionTopicGateways = instructionTopicGateway;
  // }

  public LightController() {
  }

  // Methoden
  @GetMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @CrossOrigin
  @PreAuthorize("hasAuthority('" + AppAuthority.Names.READ_AIR + "')")
  public ResponseEntity<LightStatusDto> getLightLiveData() {

    LightStatusDto lightStatusDto = this.lightService.getCurrentLightState();

    return ResponseEntity.ok().body(lightStatusDto);
  }

  @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @CrossOrigin
  @PreAuthorize("hasAuthority('" + AppAuthority.Names.READ_LIGHT + "')")
  public ResponseEntity<ResponseDto> changeState(@Valid @RequestBody InstructionDto instructionDto){
    ResponseDto response = new ResponseDto();
    boolean success = this.lightService.changeStatus(instructionDto);
    if (success){

      response.setHttpStatus(HttpStatus.OK);
      response.setMessage("Änderung and MCu gesendet");
      return ResponseEntity.ok().body(response);
    }
    response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    response.setMessage("Äneurng an MCU nicht gesendet");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
