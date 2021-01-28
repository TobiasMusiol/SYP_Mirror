package de.thkoeln.syp.iot_etage.mqtt;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import de.thkoeln.syp.iot_etage.mqtt.helper.Action;
import de.thkoeln.syp.iot_etage.mqtt.helper.MCUID;
import de.thkoeln.syp.iot_etage.mqtt.helper.PayloadKey;
import de.thkoeln.syp.iot_etage.mqtt.helper.PayloadMode;

public class MqttInstructionDto {
  private MCUID mcuId;
  private Action action;
  private Map<String, Object> payload;

  //Konstruktor
  public MqttInstructionDto(){
    this.payload = new HashMap<>();
  }

  //Methoden
  public void setAction(Action actionMode){
    this.action = actionMode;
  }

  public void setActionSwitchtMode(PayloadMode newMode){
    this.payload.put(PayloadKey.TARGET_MODE.name(), newMode);
  }

  public void setActionSetBrightnes(int brightnes){
    this.payload.put(PayloadKey.BRIGTHNESS.name(), brightnes);
  }

  public void setActionThreshold(int threshhold){
    this.payload.put(PayloadKey.THRESHOLD.name(), threshhold);
  }

  public void setActionSetSpeed(int speed){
    this.payload.put(PayloadKey.SPEED.name(), speed);
  }

  public void setActionSetState(String state){
    this.payload.put(PayloadKey.STATE.name(), state);
  }
}
