package de.thkoeln.syp.iot_etage.utils;

import de.thkoeln.syp.iot_etage.controller.dto.LightStatusDto;
import de.thkoeln.syp.iot_etage.domain.model.LightStatus;

public class LightStatusMapper {
  public static LightStatusDto convertLightStatusToLightStatusDto(LightStatus lightStatus){
    LightStatusDto lightStatusDto = new LightStatusDto();
    lightStatusDto.setState(lightStatus.getState());

    return lightStatusDto;
  }

  public static LightStatus convertLightStatusDtoToToLightStatus(LightStatusDto lightStatusDto){

    LightStatus lightStatus = new LightStatus();
    lightStatus.setState(lightStatusDto.getState());

    return lightStatus;
  }

}
