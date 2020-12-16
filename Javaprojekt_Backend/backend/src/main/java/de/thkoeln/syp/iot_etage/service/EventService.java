package de.thkoeln.syp.iot_etage.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thkoeln.syp.iot_etage.contoller.EventDto;
import de.thkoeln.syp.iot_etage.domain.entity.EventData;
import de.thkoeln.syp.iot_etage.domain.repository.EventRepository;

/**
 * EventData Service
 * 
 * @param <EventData>
 */
@Service
public class EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

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
    public List<EventData> getEvents() {
        List<EventData> events = new ArrayList<EventData>();
        this.eventRepository.findAll().forEach(event -> {
            event.setAction("test X");
            events.add(event);

        });
        return events;
    }

    /**
     * Einen Event Anzeigen
     * 
     * @param eventId
     * @return Event
     */

    public EventData getEvent(long eventId) {
        EventData event = null;

        try {
            event = (EventData) this.eventRepository.findById(eventId).get();
        } catch (Exception e) {
            logger.error("Kein Event in DB gefunden", e);
            throw e;
        }

        return event;
    }

    /**
     * Events returnen, die in einem bestimmeten Zeitinterval leigen
     * 
     * @param newEvent
     * @return
     */
    public List<EventData> getEvents(Date start, Date end) {
        List<EventData> events = new ArrayList<EventData>();

        this.eventRepository.findAll().forEach(event -> {
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
    public EventDto createEvent(EventDto newEventDto) {
        EventData newEvent = this.eventRepository.save(
            new EventData(
                "Action?", 
                "OldState",
                newEventDto.getNewState(), 
                newEventDto.getTrigger(), 
                new Date()
            )
        );
        
        return new EventDto(
            newEvent.getEventId(),
            newEvent.getAction(),
            newEvent.getOldState(),
            newEvent.getNewState(),
            newEvent.getTrigger(),
            newEvent.getTimestamp()
        );
    }

}
