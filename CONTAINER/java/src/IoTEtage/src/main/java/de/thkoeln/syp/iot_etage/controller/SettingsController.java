package de.thkoeln.syp.iot_etage.controller;

import javax.validation.Valid;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.thkoeln.syp.iot_etage.auth.helper.AppRole;
import de.thkoeln.syp.iot_etage.controller.dto.SettingsDto;
import de.thkoeln.syp.iot_etage.mqtt.MqttSubSensorHandler;
import de.thkoeln.syp.iot_etage.service.SettingsService;

@RestController
@RequestMapping(path="/settings")
public class SettingsController {

  @Autowired
  private  SettingsService settingsService;


  @GetMapping(
    path="/tb",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @CrossOrigin
  @PreAuthorize("hasAuthority('"+AppRole.Names.ADMIN+"')")
  public ResponseEntity<SettingsDto> getCurrentSettings(){
    SettingsDto settingsDto = this.settingsService.getCurrentSettings();

    return ResponseEntity.ok().body(settingsDto);
  }

  @PostMapping(
    path="/tb",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @CrossOrigin
  @PreAuthorize("hasAuthority('"+AppRole.Names.ADMIN+"')")
  public ResponseEntity<SettingsDto> setSendToTB(@Valid @RequestBody SettingsDto settingsDto){

    settingsDto = this.settingsService.changeSendToTB(settingsDto);
    
    return ResponseEntity.status(HttpStatus.OK).body(settingsDto);
  }
}
