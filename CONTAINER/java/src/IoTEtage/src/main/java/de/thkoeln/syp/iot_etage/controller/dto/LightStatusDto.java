package de.thkoeln.syp.iot_etage.controller.dto;

import javax.validation.constraints.NotNull;
import de.thkoeln.syp.iot_etage.domain.helper.State;


public class LightStatusDto {
  @NotNull
  private State state;
  private int brightness;
  private String sensorValue;

  // Constructors
  public LightStatusDto(
    State state,
    int brightnes,
    String sensorValue
  ){
    this.state = state;
    this.brightness = brightnes;
    this.sensorValue = sensorValue;
  }

  public LightStatusDto(){}

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public int getBrightness() {
    return brightness;
  }

  public void setBrightness(int brightness) {
    this.brightness = brightness;
  }

  public String getSensorValue() {
    return sensorValue;
  }

  public void setSensorValue(String sensorValue) {
    this.sensorValue = sensorValue;
  }
  
}
