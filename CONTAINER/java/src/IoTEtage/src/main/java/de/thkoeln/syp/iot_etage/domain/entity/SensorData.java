package de.thkoeln.syp.iot_etage.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "iot_etage_sensordata")
public class SensorData {
    
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="uid")
    private long uid;

    @Column(name="sensortype")
    private String sensorType;

    @Column(name="payload")
    private String payload;

    @JsonFormat(pattern = "dd.MM.yyyy, HH:mm:ss")
    @Column(
      name="timestamp",
      insertable = false,
      nullable=false
    )
    private Date timestamp;

    public SensorData(long id, long uid, String sensorType, String payload, Date timestamp){
      this.id = id;
      this.uid = uid;
      this.sensorType = sensorType;
      this.payload = payload;
      this.timestamp = timestamp;
    }

    public SensorData(){}

    public long getId(){
      return this.id;
    }

    public void setId(long id){
      this.id = id;
    }

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

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Date date) {
        this.timestamp = date;
    }


}
