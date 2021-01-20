package de.thkoeln.syp.iot_etage.service;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thkoeln.syp.iot_etage.controller.dto.LightStatusDto;
import de.thkoeln.syp.iot_etage.domain.helper.Modus;
import de.thkoeln.syp.iot_etage.domain.model.LightStatus;
import de.thkoeln.syp.iot_etage.utils.LightStatusMapper;

/**
* Service für Beleuchtungssteueurng
*/
@Service
public class LightService {
  
  private static final Logger logger = LoggerFactory.getLogger(LightService.class);
  
  private LightStatus lightStatus;

  public LightService(){
    this.lightStatus = new LightStatus();
  }
  // Methods
  /**
  * den aktuellen Zustand lesen
  */
  public LightStatusDto getCurrentLightState(){

    return LightStatusMapper.convertLightStatusToLightStatusDto(this.lightStatus);
  }
  
  /**
  * Beleuchtungssteuerung Zustand initialisieren -> wird nur vom MC ausgerufen
  */
  public LightStatusDto initLightState(LightStatusDto lightStatusDto){

    this.lightStatus.setModus(lightStatusDto.getModus());
    this.lightStatus.setLightThreshold(lightStatusDto.getLightTreshold());
    
    return LightStatusMapper.convertLightStatusToLightStatusDto(this.lightStatus);
  }

  /**
  * aktuellen Zustand ändern 
  */
  public LightStatusDto changeLightState(LightStatusDto lightStatusDto){

    if (this.lightStatus.getModus() != lightStatusDto.getModus() || this.lightStatus.getLightThreshold() != lightStatusDto.getLightTreshold()){
      
      Boolean mcuChangedModus = false;
      mcuChangedModus = sendToMCU(lightStatusDto);

      if (mcuChangedModus){
        this.lightStatus.setModus(lightStatusDto.getModus());
        this.lightStatus.setLightThreshold(lightStatusDto.getLightTreshold());
      }
    }
    
    return LightStatusMapper.convertLightStatusToLightStatusDto(this.lightStatus);
  }

  /**
  * aktuellen Zustand auf Default-Zustand setzen
  */
  public LightStatusDto setLightStateToDefault(){

    this.lightStatus.setModus(Modus.MANUEL);
    this.lightStatus.setLightThreshold(0);
    
    return LightStatusMapper.convertLightStatusToLightStatusDto(this.lightStatus);
  }

  private Boolean sendToMCU(LightStatusDto lightStatusDto){
    Boolean mcuChangedState = false;

    /* ersetzen start */
    Random rand = new Random(); //instance of random class
    int upperboundModus = 1;
    int intRandomModus = rand.nextInt(upperboundModus);

    int upperBoundLightThreshold = 100;
    int intRandomLightThreshold = rand.nextInt(upperBoundLightThreshold);

    lightStatusDto.setModus(Modus.values()[intRandomModus]);
    lightStatusDto.setLightTreshold(upperBoundLightThreshold);
    /* ersetzen end */

    mcuChangedState = true;
    return mcuChangedState;
  }
}
