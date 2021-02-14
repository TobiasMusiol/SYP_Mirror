package de.thkoeln.syp.iot_etage.controller.dto;

import javax.validation.constraints.NotNull;

import de.thkoeln.syp.iot_etage.domain.helper.State;

public class AirStatusDto {
  @NotNull
  private State state;
  private String sensorValueTemp;
  private String sensorValueHumidity;
  private String sensorValueAirPressure;

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public String getSensorValueTemp() {
    return sensorValueTemp;
  }

  public void setSensorValueTemp(String sensorValueTemp) {
    this.sensorValueTemp = sensorValueTemp;
  }

  public String getSensorValueHumidity() {
    return sensorValueHumidity;
  }

  public void setSensorValueHumidity(String sensorValueHumidity) {
    this.sensorValueHumidity = sensorValueHumidity;
  }

  public String getSensorValueAirPressure() {
    return sensorValueAirPressure;
  }

  public void setSensorValueAirPressure(String sensorValueAirPressure) {
    this.sensorValueAirPressure = sensorValueAirPressure;
  }

  
}
