package de.thkoeln.syp.iot_etage.domain.model;

import org.springframework.stereotype.Component;

import de.thkoeln.syp.iot_etage.domain.helper.Modus;

@Component
public class AirStatus {
  private Modus modus;
  private int airThresholdValue;

  public AirStatus(){
    this.modus = Modus.NO_DATA;
    this.airThresholdValue = 0;
  }

  public Modus getModus() {
    return modus;
  }

  public void setModus(Modus modus) {
    this.modus = modus;
  }

  public int getAirThresholdValue() {
    return airThresholdValue;
  }

  public void setAirThresholdValue(int airThresholdValue) {
    this.airThresholdValue = airThresholdValue;
  }
  
}
