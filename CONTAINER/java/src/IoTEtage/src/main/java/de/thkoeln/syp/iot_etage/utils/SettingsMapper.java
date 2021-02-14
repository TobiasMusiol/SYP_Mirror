package de.thkoeln.syp.iot_etage.utils;

import de.thkoeln.syp.iot_etage.controller.dto.SettingsDto;
import de.thkoeln.syp.iot_etage.domain.Settings;

public class SettingsMapper {
  public static Settings convertSettingsDtoToSettings(SettingsDto settingsDto){
    Settings settings = new Settings();

    settings.setSentToTB(settingsDto.getSendToTb());

    return settings;
  }  

  public static SettingsDto convertSettingsToSettingsDto(Settings settings){
    SettingsDto settingsDto = new SettingsDto();

    settingsDto.setSendToTb(settings.getSentToTB());

    return settingsDto;
  }
}
