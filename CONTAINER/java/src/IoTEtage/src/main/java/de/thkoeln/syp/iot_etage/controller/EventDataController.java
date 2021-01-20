package de.thkoeln.syp.iot_etage.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import de.thkoeln.syp.iot_etage.service.EventService;
import de.thkoeln.syp.iot_etage.auth.helper.AppAuthority;
import de.thkoeln.syp.iot_etage.auth.helper.AppRole;
import de.thkoeln.syp.iot_etage.controller.dto.EventDataDto;

/**
 * Event Rest Controller
 */
@Validated
@RestController
@RequestMapping(path = "/events")
public class EventDataController {
    private final EventService eventService;

    @Autowired
    public EventDataController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * RESTful GET Methode - alle Events bekommen
     */
    @GetMapping(
      path = "",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
    )
    @CrossOrigin
    @PreAuthorize("hasAuthority('"+AppAuthority.Names.READ_EVENTS+"')")
    public ResponseEntity<List<EventDataDto>> getEvents() {
        
      List<EventDataDto> eventDataDtos = this.eventService.getEvents();
        
      return ResponseEntity.ok().body(eventDataDtos);

    }

    /**
     * RESTful GET Methoden - einen bestimmen Event bekommen
     */
    @GetMapping(
      path = "/{id}", 
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = "application/json"
    )
    @CrossOrigin
    @ResponseBody
    @PreAuthorize("hasAuthority('"+AppAuthority.Names.READ_EVENTS+"')")
    public EventDataDto getEvent(@PathVariable long id) {
      
      EventDataDto eventDataDto = this.eventService.getEvent(id);

      return eventDataDto;
    }
    /**
     * RESTfull GET Methode - den neusten Event bekommen
     */
    @GetMapping(
      path = "/latest", 
      consumes = "application/json",
      produces = "application/json"
    )
    @CrossOrigin
    @PreAuthorize("hasAuthority('"+AppAuthority.Names.READ_EVENTS+"')")
    public ResponseEntity<EventDataDto> getNewEvent() {
        // hier muss man etwas mit sortieren machen
        HttpHeaders header = new HttpHeaders();
        header.set("Test-Key", "Test-Value");
        EventDataDto newestEventDataDto = this.eventService.getNewestEvent();
        return ResponseEntity.ok().headers(header).body(newestEventDataDto);
    }

    /**
     * RESTfull POST Methode - mehrere Events in DB schreiben
     */
    @PostMapping(
      path="",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE 
    )
    @CrossOrigin
    @PreAuthorize("hasAuthority('"+AppAuthority.Names.EDIT_EVENTS+"')")
    public ResponseEntity<List<EventDataDto>> createEvents(@RequestBody @NotEmpty(message = "Input movie list cannot be empty.")List<@Valid EventDataDto> eventDtoList) {
      
      List<EventDataDto> createdEvents = this.eventService.createEvents(eventDtoList);
        
      return ResponseEntity.status(HttpStatus.CREATED).body(createdEvents);
    }
}