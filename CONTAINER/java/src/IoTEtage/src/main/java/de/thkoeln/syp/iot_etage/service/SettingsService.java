package de.thkoeln.syp.iot_etage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thkoeln.syp.iot_etage.controller.dto.SettingsDto;
import de.thkoeln.syp.iot_etage.domain.Settings;
import de.thkoeln.syp.iot_etage.utils.SettingsMapper;

@Service
public class SettingsService {
  //Attribute
  @Autowired
  private Settings settings;

  
  
  //Methoden

  public SettingsDto getCurrentSettings(){
    SettingsDto settinsDto = SettingsMapper.convertSettingsToSettingsDto(this.settings);

    return settinsDto;
  }

  public SettingsDto changeSendToTB(SettingsDto settingsDto){

    if (this.settings.getSentToTB() != settingsDto.getSendToTb()){
      this.settings.setSentToTB(settingsDto.getSendToTb());
    }
    return SettingsMapper.convertSettingsToSettingsDto(this.settings);  
  }
}
