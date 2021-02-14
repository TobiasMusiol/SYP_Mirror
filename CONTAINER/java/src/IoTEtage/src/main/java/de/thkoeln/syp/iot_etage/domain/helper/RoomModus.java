package de.thkoeln.syp.iot_etage.domain.helper;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RoomModus {
  NO_DATA("NO DATA"),
  BELEGT("belegt"),
  FREI("frei"),
  REINIGEN("reinigen");

  private final String value;

  //Konstruktor

  private RoomModus(String value){
    this.value= value;
  }

  @JsonValue
  public String getValue(){
    return this.value;
  }
}
