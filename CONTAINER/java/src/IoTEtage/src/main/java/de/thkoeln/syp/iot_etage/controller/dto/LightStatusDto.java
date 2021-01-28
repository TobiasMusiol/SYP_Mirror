package de.thkoeln.syp.iot_etage.controller.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import de.thkoeln.syp.iot_etage.domain.helper.State;


public class LightStatusDto {
  @NotNull
  private State state;
  private int brightness;
  private int sensorValue;

  // Constructors
  public LightStatusDto(
    State state,
    int brightnes,
    int sensorValue
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

  public int getSensorValue() {
    return sensorValue;
  }

  public void setSensorValue(int sensorValue) {
    this.sensorValue = sensorValue;
  }
  
}
