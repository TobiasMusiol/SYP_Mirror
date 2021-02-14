package de.thkoeln.syp.iot_etage.utils;

import de.thkoeln.syp.iot_etage.controller.dto.EventDataDto;
import de.thkoeln.syp.iot_etage.domain.entity.EventData;

public class EventDataMapper {

    public static EventDataDto convertEventDataToEventDataDto(EventData evnt){
      EventDataDto eventDataDto = new EventDataDto();

      eventDataDto.setId(evnt.getEventId());
      eventDataDto.setUid(evnt.getUid());
      eventDataDto.setAction(evnt.getAction());
      eventDataDto.setOldState(evnt.getOldState());
      eventDataDto.setNewState(evnt.getNewState());
      eventDataDto.setTrigger(evnt.getTrigger());
      eventDataDto.setTimestamp(evnt.getTimestamp());

      return eventDataDto;
    }

    public static EventData convertEventDataDtoToEventData(EventDataDto evntDto){
      EventData eventData = new EventData();

      eventData.setEventId(evntDto.getId());
      eventData.setUid(evntDto.getUid());
      eventData.setAction(evntDto.getAction());
      eventData.setOldState(evntDto.getOldState());
      eventData.setNewState(evntDto.getNewState());
      eventData.setTrigger(evntDto.getTrigger());
      eventData.setTimestamp(evntDto.getTimestamp());

     return eventData;
    }

}
