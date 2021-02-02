package de.thkoeln.syp.iot_etage.domain.model;

import org.springframework.stereotype.Component;

import de.thkoeln.syp.iot_etage.domain.helper.State;


@Component
public class AirStatus {
  private State state;

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }
  
}
