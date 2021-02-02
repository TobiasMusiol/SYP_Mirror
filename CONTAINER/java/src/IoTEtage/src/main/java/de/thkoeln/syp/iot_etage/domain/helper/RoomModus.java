package de.thkoeln.syp.iot_etage.domain.helper;

public enum RoomModus {
  NO_DATA("NO DATA"),
  BELEGT("belegt"),
  FREI("frei"),
  REINIGEN("reinigen");

  private final String name;

  //Konstruktor

  private RoomModus(String name){
    this.name = name;
  }
}
