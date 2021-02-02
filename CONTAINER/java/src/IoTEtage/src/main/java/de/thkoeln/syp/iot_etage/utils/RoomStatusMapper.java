package de.thkoeln.syp.iot_etage.utils;

import de.thkoeln.syp.iot_etage.controller.dto.RoomStatusDto;
import de.thkoeln.syp.iot_etage.domain.model.RoomStatus;

public class RoomStatusMapper {
  
  public static RoomStatusDto convertRoomStatusToRoomStatusDto(RoomStatus roomStatus){

    return new RoomStatusDto(
      roomStatus.getRoomModus()
    );
  }

  public static RoomStatus  convertRoomStatusDtoToRoomStatus(RoomStatusDto roomStatusDto){

    return new RoomStatus(
      roomStatusDto.getRoomModus()
    );
  }
}
