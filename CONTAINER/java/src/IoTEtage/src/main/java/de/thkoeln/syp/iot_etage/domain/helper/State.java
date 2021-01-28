package de.thkoeln.syp.iot_etage.domain.helper;

public enum State{
  NO_DATA("no data"),
  PENDING("pending"),
  COMPLETED("completed");
  
  private final String name;

  //Konstruktor
  private State(String name){
    this.name = name;
  }
}
