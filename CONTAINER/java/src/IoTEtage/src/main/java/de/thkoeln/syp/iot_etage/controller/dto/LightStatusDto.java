package de.thkoeln.syp.iot_etage.controller.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import de.thkoeln.syp.iot_etage.domain.helper.Modus;

public class LightStatusDto {
  @NotNull
  private Modus modus;
  @PositiveOrZero
  private int lightTreshold;

  // Constructors
  public LightStatusDto(
    Modus modus,
    int lightTrheshold
  ){
    this.modus = modus;
    this.lightTreshold = lightTrheshold;
  }

  public LightStatusDto(){}
  
  //Getter + Setter
  public Modus getModus() {
    return modus;
  }

  public void setModus(Modus modus) {
    this.modus = modus;
  }

  public int getLightTreshold() {
    return lightTreshold;
  }

  public void setLightTreshold(int lightTreshold) {
    this.lightTreshold = lightTreshold;
  }
}
