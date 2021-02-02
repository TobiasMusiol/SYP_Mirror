package de.thkoeln.syp.iot_etage.auth.helper;

public enum AppAuthority {
  // Beleuchtungssteuerung
  READ_LIGHT("READ_LIGHT"),
  EDIT_LIGHT("EDIT_LIGHT"),

  //Markisolettensteuerung
  READ_AWNING("READ_AWNING"),
  EDIT_AWNING("EDIT_AWNING"),

  //Beluftungsteuerung
  READ_AIR("READ_AIR"),
  EDIT_AIR("EDIT_AIR"),

  //Raumstatuspermissions
  READ_ROOM_STATUS("READ_ROOM_STATUS"),
  EDIT_ROOM_STATUS_FM("EDIT_ROOM_STATUS_FM"),
  EDIT_ROOM_STATUS_OFFICE_WORKER("EDIT_ROOM_STATUS_OFFICE_WORKER"),

  INIT_STATUS("INIT_STATUS"),

  //Events
  READ_EVENTS("READ_EVENTS"),
  EDIT_EVENTS("EDIT_EVENTS"),

  //Sensors
  READ_SENSORS("READ_SENSORS"),
  EDIT_SENSORS("EDIT_SENSORS");
  
  private String permissionValue;

  public class Names{
    public static final String READ_LIGHT= "READ_LIGHT";
    public static final String EDIT_LIGHT= "EDIT_LIGHT";

    public static final String READ_AWNING = "READ_AWNING";
    public static final String EDIT_AWNING = "EDIT_AWNING";

    public static final String READ_AIR = "READ_AIR";
    public static final String EDIT_AIR = "EDIT_AIR";

    public static final String READ_ROOM_STATUS = "READ_ROOM_STATUS";
    public static final String EDIT_ROOM_STATUS_OFFICE_WORKER = "EDIT_ROOM_STATUS_OFFICE_WORKER";
    public static final String EDIT_ROOM_STATUS_OFFICE_FM = "EDIT_ROOM_STATUS_FM";

    public static final String INIT_STATUS = "INIT_STATUS";

    public static final String READ_EVENTS = "READ_EVENTS";
    public static final String EDIT_EVENTS = "EDIT_EVENTS";

    public static final String READ_SENSORS = "READ_SENSORS";
    public static final String EDIT_SENSORS = "EDIT_SENSORS";
  } 

  // Constructors
  AppAuthority(String permissionValue){
    this.permissionValue = permissionValue;
  }

  public String getAppAuthorityValue(){
    return this.permissionValue;
  }

  
}
