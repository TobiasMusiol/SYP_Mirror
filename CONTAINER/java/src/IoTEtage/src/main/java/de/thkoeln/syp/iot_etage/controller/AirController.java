package de.thkoeln.syp.iot_etage.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.thkoeln.syp.iot_etage.auth.helper.AppAuthority;
import de.thkoeln.syp.iot_etage.controller.dto.AirStatusDto;
import de.thkoeln.syp.iot_etage.controller.dto.InstructionDto;
import de.thkoeln.syp.iot_etage.controller.dto.ResponseDto;
import de.thkoeln.syp.iot_etage.mqtt.InstructionResponseDto;
import de.thkoeln.syp.iot_etage.service.AirService;

@RestController
@RequestMapping(path="/air")
public class AirController {

  private final AirService airService;

  //Konstruktor
  @Autowired
  public AirController(AirService airService){
    this.airService = airService;
  }
  //Methoden
  @GetMapping(
    path="",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE 
  )
  @CrossOrigin
  @PreAuthorize("hasAuthority('"+AppAuthority.Names.READ_AIR+"')")
  public ResponseEntity<AirStatusDto> getCurrentStatus(){
    AirStatusDto airStatusDto = this.airService.getCurrentState();

    return ResponseEntity.ok().body(airStatusDto);
  }

  @PostMapping(
    path="",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @CrossOrigin
  @PreAuthorize("hasAuthority('"+AppAuthority.Names.EDIT_AIR+"')")
  public ResponseEntity<ResponseDto> changeState(@Valid @RequestBody InstructionDto instructionDto){
    ResponseDto response = new ResponseDto();
    InstructionResponseDto instrResponse = this.airService.changeStatus(instructionDto);

    if (instrResponse != null ) {
      response.setHttpStatus(HttpStatus.OK);

      if(instrResponse.isSuccess()){
        response.setMessage("Änderung Fertig");
      }
      else{
        response.setMessage("Änderung nicht ausgeführt: " + instrResponse.getMessage());       
      }
      return ResponseEntity.ok().body(response);
    }

    response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    response.setMessage("Änderung an MCU nicht gesendet");

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

}
