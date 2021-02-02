package de.thkoeln.syp.iot_etage.controller.dto;

import javax.validation.constraints.NotNull;

import de.thkoeln.syp.iot_etage.domain.helper.State;

public class AwningStatusDto {
  @NotNull
  private State state;
  private int threshold;
  private String sensorValue;

  //Constructor

  //Methoden
  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public int getThreshold() {
    return threshold;
  }

  public void setThreshold(int threshold) {
    this.threshold = threshold;
  }

  public String getSensorValue() {
    return sensorValue;
  }

  public void setSensorValue(String sensorValue) {
    this.sensorValue = sensorValue;
  }
}
