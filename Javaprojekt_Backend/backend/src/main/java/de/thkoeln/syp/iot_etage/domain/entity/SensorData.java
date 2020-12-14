package de.thkoeln.syp.iot_etage.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
//@Table(name = "Sensordata")
public class SensorData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private long uid;

    private String sensorType;

    private String payload;

    @JsonFormat(pattern = "dd.MM.yyyy, HH:mm:ss")
    private Date date = new Date();

    public long getUid() {
        return this.uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getSensorType() {
        return this.sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getPayload() {
        return this.payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}
