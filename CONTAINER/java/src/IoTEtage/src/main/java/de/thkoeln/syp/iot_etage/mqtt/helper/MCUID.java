package de.thkoeln.syp.iot_etage.mqtt.helper;

public enum MCUID {
  LICHT(1001),
  AWNING(1002),
  AIR(1003),
  ROOMSTATE(1004);

  private final int mcuId;

  private MCUID(int mcuid){
    this.mcuId = mcuid;
  }
  
  public int getMcuId(){
    return this.mcuId;
  }
}
