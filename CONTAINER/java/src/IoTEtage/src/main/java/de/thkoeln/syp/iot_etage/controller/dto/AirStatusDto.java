package de.thkoeln.syp.iot_etage.controller.dto;

import javax.validation.constraints.NotNull;

import de.thkoeln.syp.iot_etage.domain.helper.State;

public class AirStatusDto {
  @NotNull
  private State state;
  private int temperatur;
  private String sensorValue;

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public int getTemperatur() {
    return temperatur;
  }

  public void setTemperatur(int temperatur) {
    this.temperatur = temperatur;
  }

  public String getSensorValue() {
    return sensorValue;
  }

  public void setSensorValue(String sensorValue) {
    this.sensorValue = sensorValue;
  }

  
}
