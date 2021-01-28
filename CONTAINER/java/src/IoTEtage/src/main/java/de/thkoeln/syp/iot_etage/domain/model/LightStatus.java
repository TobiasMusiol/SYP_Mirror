package de.thkoeln.syp.iot_etage.domain.model;

import org.springframework.stereotype.Component;

import de.thkoeln.syp.iot_etage.domain.helper.State;

@Component
public class LightStatus {
  private State state;

  public LightStatus(){
    this.state = State.NO_DATA;
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

}
