package de.thkoeln.syp.iot_etage.domain.model;

import org.springframework.stereotype.Component;

import de.thkoeln.syp.iot_etage.domain.helper.State;

@Component
public class AwningStatus {
  private State state;
  
  public AwningStatus(){
    this.state = State.NO_DATA;
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }
}
