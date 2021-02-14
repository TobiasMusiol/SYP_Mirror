package de.thkoeln.syp.iot_etage.domain;

import org.springframework.stereotype.Component;

@Component
public class Settings {
  //Attribute
  private Boolean sentToTB;

  //Konstruktoren
  public Settings(){
    this.sentToTB = false;
  }

  //Methoden
  public Boolean getSentToTB() {
    return sentToTB;
  }

  public void setSentToTB(Boolean sentToTB) {
    this.sentToTB = sentToTB;
  }

  

}
