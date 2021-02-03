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

import de.thkoeln.syp.iot_etage.service.AwningService;
import de.thkoeln.syp.iot_etage.auth.helper.AppAuthority;
import de.thkoeln.syp.iot_etage.controller.dto.AwningStatusDto;
import de.thkoeln.syp.iot_etage.controller.dto.InstructionDto;
import de.thkoeln.syp.iot_etage.controller.dto.ResponseDto;
import de.thkoeln.syp.iot_etage.mqtt.InstructionResponseDto;

@RestController
@RequestMapping(path="/awning")
public class AwningController {
  private final AwningService awningService;

  @Autowired
  public AwningController(AwningService awningService){
    this.awningService = awningService;
  }

  @GetMapping(
    path = "",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @CrossOrigin
  @PreAuthorize("hasAuthority('" + AppAuthority.Names.READ_AWNING + "')")
  public ResponseEntity<AwningStatusDto> getCurrentStatus(){

    AwningStatusDto lightStatusDto = this.awningService.getCurrentState();

    return ResponseEntity.ok().body(lightStatusDto);

  }

  @PostMapping(
    path = "",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @CrossOrigin
  @PreAuthorize("hasAuthority('" + AppAuthority.Names.EDIT_AWNING+ "')")
  public ResponseEntity<ResponseDto> changeState(@Valid @RequestBody InstructionDto instructionDto){

    ResponseDto response= new ResponseDto();

    InstructionResponseDto instrResponse = this.awningService.changeState(instructionDto);

    if (instrResponse != null){
      if(instrResponse.isSuccess()){
        response.setHttpStatus(HttpStatus.OK);
        response.setMessage("Ändrung erfolgrech angewandt"); 
      }
      else {
        response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        response.setMessage("Änderung nicht ausgeführt: " + instrResponse.getMessage()); 
      }

      return ResponseEntity.ok().body(response);
    }

    response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    response.setMessage("Änderung an MCU nicht gesendet");

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    
  }
}
