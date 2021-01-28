package de.thkoeln.syp.iot_etage.mqtt.helper;

public enum PayloadKey {
  TARGET_MODE("targetMode"),
  BRIGTHNESS("Brightness"),
  THRESHOLD("Threshold"),
  SPEED("speed"),
  STATE("state");

  private String keyName;

  private PayloadKey(String key){
    this.keyName = key;
  }
}
