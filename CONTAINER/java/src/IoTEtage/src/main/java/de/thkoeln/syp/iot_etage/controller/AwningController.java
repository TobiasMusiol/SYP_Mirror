package de.thkoeln.syp.iot_etage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.thkoeln.syp.iot_etage.service.AwningService;

@RestController
@RequestMapping(path="/awning")
public class AwningController {
  private final AwningService awningService;

  @Autowired
  public AwningController(AwningService awningService){
    this.awningService = awningService;
  }

  @GetMapping(
    path = "",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @CrossOrigin
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> getCurrentStatus(){

    ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);

    return response;
  }

  @PostMapping(
    path = "",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  @CrossOrigin
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<?> initStatus(){

    return ResponseEntity.ok().body("Not Yet Implemented");
  }
}
