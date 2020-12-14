package de.thkoeln.syp.iot_etage.contoller;

import java.util.Date;

public class EventDto {

    private long id;
    private String action;
    private String oldState;
    private String newState;
    private String trigger;
    private Date date;

    // Konstruktoren
    public EventDto(String action, String oldState, String newState, String trigger, Date date) {
        this.action = action;
        this.oldState = action;
        this.newState = newState;
        this.trigger = trigger;
        this.date = date;
    }

    public EventDto(long id, String action, String oldState, String newState, String trigger, Date date) {
        this(action, oldState, newState, trigger, date);
        this.id = id;
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

}
