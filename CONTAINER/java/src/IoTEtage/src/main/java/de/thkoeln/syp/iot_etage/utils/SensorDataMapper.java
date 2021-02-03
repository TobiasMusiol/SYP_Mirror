package de.thkoeln.syp.iot_etage.utils;

import java.util.Date;

import de.thkoeln.syp.iot_etage.controller.dto.SensorDataDto;
import de.thkoeln.syp.iot_etage.domain.entity.SensorData;

/**
 * SensorDataMapper
 */
public class SensorDataMapper {

  public static SensorData convertSensorDataDtoToSensorData(SensorDataDto sensorDataDto){
    return new SensorData(
      sensorDataDto.getId(),
      sensorDataDto.getUid(),
      sensorDataDto.getSensorType(),
      sensorDataDto.getPayload(),
      new Date()
    );
  }

  public static SensorDataDto convertSensorDataToSensorDataDto(SensorData sensorData){
    return new SensorDataDto(
      sensorData.getId(),
      sensorData.getUid(),
      sensorData.getSensorType(),
      sensorData.getPayload(),
      sensorData.getTimeStamp()
    );
  }
}