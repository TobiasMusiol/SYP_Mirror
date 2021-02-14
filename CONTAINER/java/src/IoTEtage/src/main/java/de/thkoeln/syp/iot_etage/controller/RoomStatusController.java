package de.thkoeln.syp.iot_etage.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import de.thkoeln.syp.iot_etage.auth.helper.AppAuthority;
import de.thkoeln.syp.iot_etage.controller.dto.InstructionDto;
import de.thkoeln.syp.iot_etage.controller.dto.ResponseDto;
import de.thkoeln.syp.iot_etage.controller.dto.RoomStatusDto;
import de.thkoeln.syp.iot_etage.mqtt.InstructionResponseDto;
import de.thkoeln.syp.iot_etage.service.RoomStatusService;

@RestController
@RequestMapping(path="/roomstate")
public class RoomStatusController {
  
  private final RoomStatusService roomStatusService;

  @Autowired
  public RoomStatusController(RoomStatusService roomStatusService){
    this.roomStatusService = roomStatusService;
  }

  @GetMapping(
    path="",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @CrossOrigin
  @PreAuthorize("hasAuthority('" + AppAuthority.Names.READ_ROOM_STATUS+"')")
  public ResponseEntity<RoomStatusDto> getCurrentRoomStatus(){

    RoomStatusDto roomStatusDto = this.roomStatusService.getCurrentRoomStatus();

    return ResponseEntity.ok().body(roomStatusDto);
  }

  @PostMapping(
    path = "", 
    consumes = MediaType.APPLICATION_JSON_VALUE, 
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @CrossOrigin
  @PreAuthorize("hasAuthority('" + AppAuthority.Names.EDIT_ROOM_STATUS+ "')")
  public ResponseEntity<ResponseDto> changeState(@Valid @RequestBody InstructionDto instructionDto){

    ResponseDto response = new ResponseDto();

    InstructionResponseDto instrResponse = this.roomStatusService.changeRoomStatus(instructionDto);

    if (instrResponse != null){
      response.setHttpStatus(HttpStatus.OK);

      if(instrResponse.isSuccess()){
        response.setMessage("Änderung erfolgreich angewandt");
      }
      else {
        response.setMessage("Änderung nicht ausgefürht: " +instrResponse.getMessage());
      }

      return ResponseEntity.ok().body(response);
    }

    response.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    response.setMessage("Änderung an MCU nicht gesendet");

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
