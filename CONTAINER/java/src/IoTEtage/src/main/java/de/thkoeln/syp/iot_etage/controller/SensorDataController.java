package de.thkoeln.syp.iot_etage.controller;

import de.thkoeln.syp.iot_etage.service.SensorService;
import de.thkoeln.syp.iot_etage.auth.helper.AppAuthority;
import de.thkoeln.syp.iot_etage.controller.dto.SensorDataDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/sensors")
public class SensorDataController {

    @Autowired
    private SensorService sensorService;

    //Konstruktor
    public SensorDataController(SensorService sensorService){
      this.sensorService = sensorService;
    }
    
    //Methods 
    @CrossOrigin
    @GetMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('"+AppAuthority.Names.READ_SENSORS+"')")
    public ResponseEntity<List<SensorDataDto>> getSensorData(){

      List<SensorDataDto> sensorDataDtoList = sensorService.findAll();

      return ResponseEntity.ok(sensorDataDtoList);
    }

    /*
    @PostMapping(
      value="",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CrossOrigin
    //@PreAuthorize("hasAuthority('"+AppAuthority.Names.EDIT_SENSORS+"')")
    public ResponseEntity<List<SensorDataDto>> insertSensorData(@RequestBody @NotEmpty(message = "Input dar nicht leer sein")  List<@Valid SensorDataDto> sensorDataDtos){

      List<SensorDataDto> createdSensorDataDtos = this.sensorService.insertSensorDatas(sensorDataDtos);

      return ResponseEntity.status(HttpStatus.CREATED).body(createdSensorDataDtos);
    }
    */
}
