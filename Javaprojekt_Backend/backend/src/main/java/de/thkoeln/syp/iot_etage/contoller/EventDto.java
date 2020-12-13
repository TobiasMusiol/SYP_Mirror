package de.thkoeln.syp.iot_etage.contoller;

import java.util.Date;

public class EventDto {

    private String name;

    private Date timestamp;

    // Getter und Setter
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
