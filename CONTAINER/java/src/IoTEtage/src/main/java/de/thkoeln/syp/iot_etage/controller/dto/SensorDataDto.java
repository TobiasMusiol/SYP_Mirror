package de.thkoeln.syp.iot_etage.controller.dto;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

public class SensorDataDto {

  private long id;
  @Positive
  private long uid; 
  @NotEmpty
  private String sensorType;
  @NotEmpty
  private String payload;
  @NotEmpty
  private Date timestamp;

  public SensorDataDto(long id, long uid, String sensorType, String payload, Date timestamp) {
    this.id = id;
    this.uid = uid;
    this.sensorType = sensorType;
    this.payload = payload;
    this.timestamp = timestamp;
  }

  public SensorDataDto(){}

  public long getId() {
    return this.id;
  }

  public void setId(long id) {
    this.id = id;
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

  @Override
  public String toString() {
    return "{" + " id='" + getId() + "'" + ", sensorType='" + getSensorType() + "'" + ", payload='" + getPayload() + "'" + "}";
  }

  public long getUid() {
    return uid;
  }

  public void setUid(long uid) {
    this.uid = uid;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }


}
