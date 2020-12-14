package de.thkoeln.syp.iot_etage.contoller;

import de.thkoeln.syp.iot_etage.domain.entity.SensorData;
import de.thkoeln.syp.iot_etage.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/sensorservice")
public class SensorController {

    @Autowired
    private SensorService sensorService;

    @RequestMapping(
            value="/sensordata/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getSensorDataById(@PathVariable long id){

        Optional<SensorData> foundObject = sensorService.findById(id);
        if(foundObject.isPresent()) {
            SensorData sensorData = foundObject.get();
            return ResponseEntity.ok(sensorData);
        }
        return new ResponseEntity<>("No Sernsordata with ID: " + id,HttpStatus.OK);


    }

    @RequestMapping(
            value="/sensordata",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getSensorDataById(){
        List<SensorData> sensorDataList = sensorService.findAll();
        return ResponseEntity.ok(sensorDataList);
    }

    @RequestMapping(
            value="/sensordata",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> insertSensorData(SensorData sensorData){
        Boolean success = sensorService.insertSensorData(sensorData);
        if(success)
            return new ResponseEntity("Insertion successfull.",HttpStatus.OK);
        else
            return new ResponseEntity("Insertion failed.",HttpStatus.OK);
    }

    @RequestMapping(
            value="/sensordata",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> insertSensorDatas(List<SensorData> sensorData){
        Boolean success = sensorService.insertSensorDatas(sensorData);
        if(success)
            return new ResponseEntity("Insertion successfull.",HttpStatus.OK);
        else
            return new ResponseEntity("Insertion failed.",HttpStatus.OK);
    }



}
