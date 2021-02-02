package de.thkoeln.syp.iot_etage.mqtt.helper;

public enum Action {
  //Alle
  SWITCH_MODE("switchMode"),

  //Markisoletten + Lüftunge + Raumstatus
  SET_THREASHOLD("setThreshold"),

  //Beleuchtung
  SET_BRIGHTNESS("setBrightness"),

  //Lüftung
  SET_SPEED("setSpeed"),

  //Raumstatus
  SET_STATE("setState");

  private final String action;

  //Konstruktoren

  Action(String action){
    this.action = action;
  }

  public String getAction(){
    return this.action;
  }
  
}
