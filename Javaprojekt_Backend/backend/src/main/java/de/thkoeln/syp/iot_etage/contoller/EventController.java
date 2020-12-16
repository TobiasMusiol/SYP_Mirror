package de.thkoeln.syp.iot_etage.contoller;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.thkoeln.syp.iot_etage.domain.entity.EventData;
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
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> getEvents() {
        List<EventData> events = this.eventService.getEvents();

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
    @GetMapping(path = "/{id}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public EventDto getEvent(@PathVariable long id) {
        EventData event = this.eventService.getEvent(id);
        return new EventDto(event.getEventId(), event.getAction(), event.getOldState(), event.getNewState(),
                event.getTrigger(), event.getTimestamp());
    }

    /**
     * RESTfull GET Methode - den neusten Event bekommen
     */
    @GetMapping(path = "/new", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> getNewEvent() {

        return ResponseEntity.ok().body("New");
    }

    /**
     * RESTful POST Methode - Event ertellen
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EventDto> createEvent(@RequestHeader HttpHeaders httpHeaders,
            @RequestBody EventDto newEventDto) {
        System.out.println("Header " + httpHeaders.get("user").get(0));
        System.out.println(httpHeaders.get("user").get(0).equals("Kirill"));
        if (!httpHeaders.get("user").get(0).equals("Kirill")) {

            return new ResponseEntity<EventDto>(HttpStatus.METHOD_NOT_ALLOWED);
        }

        EventDto createdEvent = this.eventService.createEvent(newEventDto);

        return new ResponseEntity<EventDto>(createdEvent, HttpStatus.CREATED);
    }
}

// @ResonseBody => was returnt wird wird zu http-Body; http-Header wird von
// Spring automatisch generiert
// ResponseEntity => selbst http-Body und http-Header bilden