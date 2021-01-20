package de.thkoeln.syp.iot_etage.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thkoeln.syp.iot_etage.controller.dto.RoomStatusDto;
import de.thkoeln.syp.iot_etage.domain.helper.RoomModus;
import de.thkoeln.syp.iot_etage.domain.model.RoomStatus;
import de.thkoeln.syp.iot_etage.utils.RoomStatusMapper;

@Service
public class RoomStatusService {
  
  private RoomStatus roomStatus;

  // Constructoren
  public RoomStatusService(){
    this.roomStatus = new RoomStatus();
  }

  //Methoden
  public RoomStatusDto getCurrentRoomStatus(){
    return RoomStatusMapper.convertRoomStatusToRoomStatusDto(this.roomStatus);
  }

  public RoomStatusDto changeRoomStatus(RoomStatusDto roomStatusDto)throws Exception{
    if(this.roomStatus.getRoomModus() != roomStatusDto.getRoomModus()){
      RoomStatus roomStatus = RoomStatusMapper.convertRoomStatusDtoToRoomStatus(roomStatusDto);
      
      Boolean mcuChangedStatus = this.sendToMCU(this.roomStatus);
      
      if(!mcuChangedStatus){
        // mit CustomExpcetion ersetzen
        throw new Exception("Fehler passiert");
      }
    }
    return RoomStatusMapper.convertRoomStatusToRoomStatusDto(this.roomStatus);
  }

  //Private Methoden
  private Boolean sendToMCU(RoomStatus roomStatus){
    Boolean mcuChangedState = false;

    /* ersetzen start */
    Random rand = new Random(); //instance of random class
    int upperbound = 2;
    int intRandom = rand.nextInt(upperbound);
    this.roomStatus.setRoomModus(RoomModus.values()[intRandom]);
    /* ersetzen end */

    mcuChangedState = true;
    return mcuChangedState;
  }
}
