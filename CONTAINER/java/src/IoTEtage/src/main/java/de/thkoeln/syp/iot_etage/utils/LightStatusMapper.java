package de.thkoeln.syp.iot_etage.utils;

import de.thkoeln.syp.iot_etage.controller.dto.LightStatusDto;
import de.thkoeln.syp.iot_etage.domain.model.LightStatus;

public class LightStatusMapper {
  public static LightStatusDto convertLightStatusToLightStatusDto(LightStatus lightStatus){

    return new LightStatusDto(
      lightStatus.getModus(),
      lightStatus.getLightThreshold()
    );
  }

  public static LightStatus convertLightStatusDtoToToLightStatus(LightStatusDto lightStatusDto){

    LightStatus lightStatus = new LightStatus();
    lightStatus.setModus(lightStatusDto.getModus());
    lightStatus.setLightThreshold(lightStatusDto.getLightTreshold());

    return lightStatus;
  }

}
