package de.thkoeln.syp.iot_etage.controller;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.thkoeln.syp.iot_etage.auth.helper.AppAuthority;
import de.thkoeln.syp.iot_etage.controller.dto.LightStatusDto;
import de.thkoeln.syp.iot_etage.service.LightService;

/**
 * Beleuchtungssteuerung Rest Controller
 */
// @Validated
@RestController
@RequestMapping(path="/light")
public class LightController {

    private final LightService lightService;
  
    @Autowired
    public LightController(LightService lightService){
      this.lightService = lightService;
    }

    @GetMapping(
      path = "",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CrossOrigin
    //@Secured(AppAuthority.Names.ROLE_READ_AIR)
    //@RolesAllowed(AppAuthority.Names.ROLE_READ_AIR)
    @PreAuthorize("hasAuthority('"+AppAuthority.Names.READ_AIR+"')")
    public ResponseEntity<LightStatusDto>getLightLiveData(){
      
      LightStatusDto lightStatusDto = this.lightService.getCurrentLightState();

      return ResponseEntity.ok().body(lightStatusDto);
    }

    @PostMapping(
      path = "",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CrossOrigin
    @Secured(AppAuthority.Names.INIT_STATUS)
    public ResponseEntity<LightStatusDto> setInitLightModus(@RequestParam @Valid LightStatusDto lightStatusDto){

      return ResponseEntity.ok().body(lightStatusDto);
    }

    @PutMapping(
      path = "",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CrossOrigin
    @Secured(AppAuthority.Names.EDIT_LIGHTCONTROLS)
    public ResponseEntity<LightStatusDto> changeLightModus(@RequestParam @Valid LightStatusDto lightStatusDto){
      
      return ResponseEntity.ok().body(lightStatusDto);
    
    }

    @DeleteMapping(
      path = "",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> setLightModusToDefault(@RequestParam @Valid LightStatusDto lightstatusDto){

      return ResponseEntity.ok().body(lightstatusDto);
    }
  
}
