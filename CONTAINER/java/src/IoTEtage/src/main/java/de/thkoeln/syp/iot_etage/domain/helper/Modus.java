package de.thkoeln.syp.iot_etage.domain.helper;

public enum Modus {
  NO_DATA("NO DATA"),
  AUTOMATIC("Automatisch"),
  MANUEL("Mauell");
  
  private final String name;

  //Konstruktor
  private Modus(String name){
    this.name = name;
  }
}
