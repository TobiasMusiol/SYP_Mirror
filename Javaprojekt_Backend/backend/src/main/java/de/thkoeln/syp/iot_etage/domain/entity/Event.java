package de.thkoeln.syp.iot_etage.domain.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Event beinhaltet die Zustandänderungen des Mikrokontrollers
 */
@Entity
@Table(name = "event")
public class Event {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long eventId;

    @Column(name = "action")
    private String action;

    @Column(name = "old_state")
    private String oldState;

    @Column(name = "new_state")
    private String trigger;

    @Column(name = "timestamp")
    private Date timestamp;

    // Konstruktoren
    public Event(String action, Date timestamp) {
        this.action = action;
        this.timestamp = timestamp;
    }

    public Event() {
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

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

}