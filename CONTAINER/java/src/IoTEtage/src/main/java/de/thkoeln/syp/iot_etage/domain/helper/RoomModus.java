package de.thkoeln.syp.iot_etage.domain.helper;

public enum RoomModus {
  LUEFTEN("Lüften"),
  MEETING("Meeting"),
  AUFRAUMEN("Aufraumen");

  private final String name;

  //Konstruktor

  private RoomModus(String name){
    this.name = name;
  }
}
