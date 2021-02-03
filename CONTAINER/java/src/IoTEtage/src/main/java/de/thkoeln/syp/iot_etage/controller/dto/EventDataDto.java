package de.thkoeln.syp.iot_etage.controller.dto;

import java.util.Date;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EventDataDto {

    private long id;
    @Positive
    @JsonProperty("UID")
    private int uid;
    private SensorType sensorType;
    @NotEmpty(message = "Aktion darf nicht leer sein.")
    private String action;

    @NotEmpty(message = "Alter Zustand darf nicht leer sein.")
    private String oldState;

    @NotEmpty(message = "Neuer Zustand darf nicht leer sein.")
    private String newState;

    @NotEmpty(message = "Trigger darf nicht leer sein.")
    private String trigger;
    
    private Date timestamp;

    // Konstruktoren
    public EventDataDto(
      long id, 
      String action, 
      String oldState, 
      String newState, 
      String trigger, 
      Date timestamp
    ){
      this.id = id;
      this.action = action;
      this.oldState = oldState;
      this.newState = newState;
      this.trigger = trigger;
      this.timestamp = timestamp;
    }

    public EventDataDto() {
    }

    // Getter und Setter
    public long getId() {
      return this.id;
    }

    public void setId(long id) {
      this.id = id;
    }

    public String getNewState() {
      return this.newState;
    }

    public void setNewState(String newState) {
      this.newState = newState;
    }

    public String getAction() {
      return this.action;
    }

    public void setAction(String action) {
      this.action = action;
    }

    public String getOldState() {
      return this.oldState;
    }

    public void setOldState(String oldState) {
      this.oldState = oldState;
    }

    public String getTrigger() {
      return this.trigger;
    }

    public void setTrigger(String trigger) {
      this.trigger = trigger;
    }

    public Date getTimestamp() {
      return this.timestamp;
    }

    public void setTimestamp(Date date) {
      this.timestamp = date;
    }

  @Override
  public String toString() {
    return "{" + " id='" + getId() + "'" + ", action='" + getAction() + "'" + ", oldState='" + getOldState() + "'" + ", newState='" + getNewState() + "'" + ", trigger='" + getTrigger() + "'" + ", timestamp='" + getTimestamp() + "'" + "}";
  }

  public int getUid() {
    return uid;
  }

  public void setUid(int uid) {
    this.uid = uid;
  }

  public SensorType getSensorType() {
    return sensorType;
  }

  public void setSensorType(SensorType sensorType) {
    this.sensorType = sensorType;
  }
}
