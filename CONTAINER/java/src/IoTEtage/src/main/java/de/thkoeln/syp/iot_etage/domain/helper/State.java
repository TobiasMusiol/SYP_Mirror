package de.thkoeln.syp.iot_etage.domain.helper;

public enum State{
  NO_DATA("no data"),
  AUTO("auto"),
  MAN("man");
  
  private final String name;

  //Konstruktor
  private State(String name){
    this.name = name;
  }
}
