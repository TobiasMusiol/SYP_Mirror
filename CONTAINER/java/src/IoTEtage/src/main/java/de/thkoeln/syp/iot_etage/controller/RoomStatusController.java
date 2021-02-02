package de.thkoeln.syp.iot_etage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import de.thkoeln.syp.iot_etage.controller.dto.RoomStatusDto;
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
  public ResponseEntity<RoomStatusDto> getCurrentRoomStatus(){

    RoomStatusDto roomStatusDto = this.roomStatusService.getCurrentRoomStatus();

    return ResponseEntity.ok().body(roomStatusDto);
  }
}
