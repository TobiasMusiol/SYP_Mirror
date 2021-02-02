package de.thkoeln.syp.iot_etage.controller.dto;

import javax.validation.constraints.NotNull;

import de.thkoeln.syp.iot_etage.domain.helper.RoomModus;

public class RoomStatusDto {
  @NotNull(message = "RoomStatus darf nicht NULL sein")
  private RoomModus roomModus;

  public RoomStatusDto(RoomModus roomModus){
    this.roomModus = roomModus;
  }

  public RoomStatusDto(){}

  public RoomModus getRoomModus() {
    return roomModus;
  }

  public void setRoomModus(RoomModus roomModus) {
    this.roomModus = roomModus;
  }
}
