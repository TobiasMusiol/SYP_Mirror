package de.thkoeln.syp.iot_etage.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.thkoeln.syp.iot_etage.controller.dto.EventDataDto;
import de.thkoeln.syp.iot_etage.domain.entity.EventData;
import de.thkoeln.syp.iot_etage.domain.repository.EventRepository;
import de.thkoeln.syp.iot_etage.utils.EventDataMapper;

/**
 * EventData Service
 * 
 * @param <EventData>
 */
@Service
public class EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;


    //Konstruktor
    @Autowired
    public EventService(
      EventRepository eventRepository 
    ) {
        this.eventRepository = eventRepository;
    }

    //Methoden

    /**
     * Alle Events aus DB holen
     * 
     * @param: None - keien Parameter
     *
     * @return: List<Event> - Liste mit allen Events
     */
    public List<EventDataDto> getEvents() {
        List<EventDataDto> eventDataDtos = new ArrayList<EventDataDto>();
        this.eventRepository.findAll().forEach(event -> {
            eventDataDtos.add(EventDataMapper.convertEventDataToEventDataDto(event));
        });
        return eventDataDtos;
    }

    /**
     * Einen Event Anzeigen
     * 
     * @param eventId
     * @return Event
     */

    public EventDataDto getEvent(long eventId) {
        EventDataDto eventDataDto = null;

        try {
            eventDataDto = EventDataMapper.convertEventDataToEventDataDto(this.eventRepository.findById(eventId).get());
        } catch (Exception e) {
            logger.error("Kein Event in DB gefunden", e);
            throw e;
        }

        return eventDataDto;
    }

    /**
     * einen neuen Event erzeugen
     * @param EventDataDto
     * 
     * @return EventDataDto
     */
    public EventDataDto createEvent(EventDataDto newEventDataDto){
      if(newEventDataDto != null){

        EventData newEventData = EventDataMapper.convertEventDataDtoToEventData(newEventDataDto);
        newEventData = this.eventRepository.save(newEventData);      
        newEventDataDto = EventDataMapper.convertEventDataToEventDataDto(newEventData);
      }

      return newEventDataDto;
    }

    /**
     * Neuen Events erzeugen
     * 
     * @param: event - neues Event
     * 
     * @return neue angelegtes Event
     */
    public List<EventDataDto> createEvents(List<EventDataDto> newEventDataDtos) {
        List<EventDataDto> createdEventDtos = new ArrayList<EventDataDto>();
        
        final List<EventData> newEvents = new ArrayList<EventData>();

        newEventDataDtos.forEach(eventDto -> {
          newEvents.add(EventDataMapper.convertEventDataDtoToEventData(eventDto));
        });

        List<EventData> newCreatedEvents = this.eventRepository.saveAll(newEvents);
        
        newCreatedEvents.forEach(event -> {
          createdEventDtos.add(EventDataMapper.convertEventDataToEventDataDto(event));
        });
        
        return createdEventDtos;
    }

}
