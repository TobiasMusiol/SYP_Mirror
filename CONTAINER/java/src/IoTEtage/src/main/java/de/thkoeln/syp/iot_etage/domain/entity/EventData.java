package de.thkoeln.syp.iot_etage.domain.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Event beinhaltet die Zustandänderungen des Mikrokontrollers
 */
@Entity
@Table(name = "iot_etage_eventdata")
public class EventData {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long eventId;

  @Column(name = "uid")
  private int uid;

  @Column(name = "action")
  private String action;

  @Column(name = "oldstate")
  private String oldState;

  @Column(name = "newstate")
  private String newState;

  @Column(name = "trigger")
  private String trigger;

  @JsonFormat(pattern = "dd.MM.yyyy, HH:mm:ss")
  @Column(
    name = "timestamp",
    insertable = false,
    nullable=true
  )
  private Date timestamp;

  // Konstruktoren
  public EventData(int uid, String action, String oldState, String newStage, String trigger, Date timestamp) {
    this.uid = uid;
    this.action = action;
    this.oldState = oldState;
    this.newState = newStage;
    this.trigger = trigger;
    this.timestamp = timestamp;
  }

  public EventData() {
  }

  public long getEventId() {
    return this.eventId;
  }

  public void setEventId(long eventId) {
    this.eventId = eventId;
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

  public String getNewState() {
    return this.newState;
  }

  public void setNewState(String newState) {
    this.newState = newState;
  }

  public void setTrigger(String trigger) {
    this.trigger = trigger;
  }

  public Date getTimestamp() {
    return this.timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public int getUid() {
    return uid;
  }

  public void setUid(int uid) {
    this.uid = uid;
  }
}