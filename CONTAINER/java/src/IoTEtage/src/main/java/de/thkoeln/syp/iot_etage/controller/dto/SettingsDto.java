package de.thkoeln.syp.iot_etage.controller.dto;

import javax.validation.constraints.NotNull;

public class SettingsDto {
  //Attribute
  @NotNull
  private Boolean sendToTb;

  //Methoden
  public Boolean getSendToTb() {
    return sendToTb;
  }

  public void setSendToTb(Boolean sendToTb) {
    this.sendToTb = sendToTb;
  }

}
