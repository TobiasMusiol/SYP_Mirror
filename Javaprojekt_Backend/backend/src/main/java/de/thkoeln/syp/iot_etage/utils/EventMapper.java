package de.thkoeln.syp.iot_etage.utils;

import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Env;
import de.thkoeln.syp.iot_etage.contoller.EventDto;
import de.thkoeln.syp.iot_etage.domain.entity.EventData;

public class EventMapper {

    public static EventDto convertEventDataToEventDto(EventData evnt){
        return new EventDto(
            evnt.getEventId(),
            evnt.getAction(),
            evnt.getOldState(),
            evnt.getNewState(),
            evnt.getTrigger(),
            evnt.getTimestamp()
        );
    }

    public static EventData convertEventDtoToEventData(EventDto evntDto){
        EventData event = new EventData(
            evntDto.getAction(),
            evntDto.getOldState(),
            evntDto.getNewState(),
            evntDto.getTrigger(),
            evntDto.getTimestamp()
        );
        event.setEventId(evntDto.getId());
        
        return event;
    }

}
