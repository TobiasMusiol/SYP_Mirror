package de.thkoeln.syp.iot_etage.contoller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.thkoeln.syp.iot_etage.domain.entity.Event;
import de.thkoeln.syp.iot_etage.service.EventService;

/**
 * Event Rest Controller
 */
@RestController
@RequestMapping(path = "/events")
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * RESTful GET Methode - alle Events bekommen
     */
    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<String> getEvents() {
        List<Event> events = this.eventService.getEvents();

        List<EventDto> eventsDto = new ArrayList<EventDto>();
        events.forEach(event -> {
            eventsDto.add(new EventDto(event.getAction(), event.getOldState(), event.getNewState(), event.getTrigger(),
                    event.getTimestamp()));
        });

        return ResponseEntity.ok().body(events.toString());

    }

    /**
     * RESTful GET Methoden - einen bestimmen Event bekommen
     */

    /**
     * RESTfull GET Methode - den neusten Event bekommen
     */

    /**
     * RESTful POST Methode - Event ertellen
     */
    @PostMapping
    @ResponseStatus
    public ResponseEntity<String> createEvent() {
        return ResponseEntity.ok();
    }
}