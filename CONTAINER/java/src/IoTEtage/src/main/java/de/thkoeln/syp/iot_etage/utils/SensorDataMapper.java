package de.thkoeln.syp.iot_etage.utils;

import de.thkoeln.syp.iot_etage.controller.dto.SensorDataDto;
import de.thkoeln.syp.iot_etage.domain.entity.SensorData;

/**
 * SensorDataMapper
 */
public class SensorDataMapper {

  public static SensorData convertSensorDataDtoToSensorData(SensorDataDto sensorDataDto){
    SensorData sensorData = new SensorData();

    sensorData.setId(sensorDataDto.getId());
    sensorData.setUid(sensorDataDto.getUid());
    sensorData.setSensorType(sensorDataDto.getSensorType());
    sensorData.setPayload(sensorDataDto.getPayload());
    sensorData.setTimestamp(sensorDataDto.getTimestamp());

    return sensorData;
  }

  public static SensorDataDto convertSensorDataToSensorDataDto(SensorData sensorData){
    SensorDataDto sensorDataDto = new SensorDataDto();

    sensorDataDto.setId(sensorData.getId());
    sensorDataDto.setUid(sensorData.getUid());
    sensorDataDto.setSensorType(sensorData.getSensorType());
    sensorDataDto.setPayload(sensorData.getPayload());
    sensorDataDto.setTimestamp(sensorData.getTimestamp());

    return sensorDataDto;
  }
}