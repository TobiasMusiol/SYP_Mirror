package de.thkoeln.syp.iot_etage.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thkoeln.syp.iot_etage.contoller.EventDto;
import de.thkoeln.syp.iot_etage.domain.entity.Event;
import de.thkoeln.syp.iot_etage.domain.repository.EventRepository;

/**
 * Event Service
 * 
 * @param <Event>
 */
@Service
public class EventService {
    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     * Alle Events anzeigen lassen
     * 
     * @param: None - keien Parameter
     *
     * @return: List<Event> - Liste mit allen Events
     */
    public List<Event> getEvents() {
        List<Event> events = new ArrayList<Event>();
        this.eventRepository.findAll().forEach(event -> {
            event.setAction("test X");
            events.add(event);

        });
        return events;
    }

    /**
     * Neuen Event erzeugen
     * 
     * @param: event - neues Event
     * 
     * @return neue angelegtes Event
     */
    public Event createEvent(EventDto newEvent) {
        return this.eventRepository.save(new Event(newEvent.getName(), newEvent.getTimestamp()));
    }

}