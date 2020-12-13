package de.thkoeln.syp.iot_etage.contoller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping
    public List<Event> getEvents() {
        return this.eventService.getEvents();
    }
}