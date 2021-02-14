package de.thkoeln.syp.iot_etage.controller.dto;

import javax.validation.constraints.NotNull;

import de.thkoeln.syp.iot_etage.domain.helper.RoomModus;

public class RoomStatusDto {
  @NotNull(message = "RoomStatus darf nicht NULL sein")
  private RoomModus roomModus;
  private String airQuality;
  private String sensorValue;

  public RoomStatusDto(){}

  public RoomModus getRoomModus() {
    return roomModus;
  }

  public void setRoomModus(RoomModus roomModus) {
    this.roomModus = roomModus;
  }

  public String getAirQuality() {
    return airQuality;
  }

  public void setAirQuality(String airQuality) {
    this.airQuality = airQuality;
  }

  public String getSensorValue() {
    return sensorValue;
  }

  public void setSensorValue(String sensorValue) {
    this.sensorValue = sensorValue;
  }
}
