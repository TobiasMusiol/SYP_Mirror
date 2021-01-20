package de.thkoeln.syp.iot_etage.domain.model;

import de.thkoeln.syp.iot_etage.domain.helper.Modus;

public class LightStatus {
  private Modus modus;
  private int lightThreshold;

  public LightStatus(){
    this.modus = Modus.NO_DATA;
    this.lightThreshold = 0;
  }

  public Modus getModus() {
    return modus;
  }

  public void setModus(Modus modus) {
    this.modus = modus;
  }

  public int getLightThreshold() {
    return lightThreshold;
  }

  public void setLightThreshold(int lightThreshold) {
    this.lightThreshold = lightThreshold;
  }
  
}
