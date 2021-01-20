package de.thkoeln.syp.iot_etage.domain.model;

import de.thkoeln.syp.iot_etage.domain.helper.RoomModus;

public class RoomStatus {
  private RoomModus roomModus;

  //Konstruktor
  public RoomStatus(RoomModus roomModus){
    this.roomModus = roomModus;
  }

  public RoomStatus(){}

  //Getter + Setter
  public RoomModus getRoomModus() {
    return roomModus;
  }

  public void setRoomModus(RoomModus roomModus) {
    this.roomModus = roomModus;
  }

}
