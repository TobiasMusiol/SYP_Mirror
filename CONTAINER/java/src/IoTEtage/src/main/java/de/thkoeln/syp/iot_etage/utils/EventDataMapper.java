package de.thkoeln.syp.iot_etage.utils;

import de.thkoeln.syp.iot_etage.controller.dto.EventDataDto;
import de.thkoeln.syp.iot_etage.domain.entity.EventData;

public class EventDataMapper {

    public static EventDataDto convertEventDataToEventDataDto(EventData evnt){

        return new EventDataDto(
            evnt.getEventId(),
            evnt.getAction(),
            evnt.getOldState(),
            evnt.getNewState(),
            evnt.getTrigger(),
            evnt.getTimestamp()
        );
    }

    public static EventData convertEventDataDtoToEventData(EventDataDto evntDto){
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
