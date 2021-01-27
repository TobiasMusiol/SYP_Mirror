package de.thkoeln.syp.iot_etage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.thkoeln.syp.iot_etage.auth.helper.AppAuthority;
import de.thkoeln.syp.iot_etage.service.AirService;

@RestController
@RequestMapping(path="/air")
public class AirController {

  private final AirService airService;

  //Konstruktor
  @Autowired
  public AirController(AirService airService){
    this.airService = airService;
  }
  //Methoden
  @GetMapping(
    path="",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE 
  )
  @CrossOrigin
  @PreAuthorize("hasAuthority('"+AppAuthority.Names.EDIT_AIR+"')")
  public void testMqtt(){
    this.airService.testMqtt();
  }
}
