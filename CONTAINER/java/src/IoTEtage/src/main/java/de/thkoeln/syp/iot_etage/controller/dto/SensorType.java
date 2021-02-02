package de.thkoeln.syp.iot_etage.controller.dto;

public enum SensorType {
  AIR("air"),
  AWNING("awning"),
  ROOM_STATUS("roomstatus"),
  LIGHT("light");

  private final String name;

  private SensorType(String name){
    this.name = name;
  }
}
