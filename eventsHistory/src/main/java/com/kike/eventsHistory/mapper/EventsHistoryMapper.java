package com.kike.eventsHistory.mapper;

import com.kike.eventsHistory.dto.EventsHistoryDto;
import com.kike.eventsHistory.entity.EventsHistory;

public class EventsHistoryMapper {

    public static EventsHistory mapToEventsHistory(EventsHistoryDto eventsHistoryDto, EventsHistory eventsHistory) {


        eventsHistory.setHistoryType(eventsHistoryDto.getHistoryType());
        eventsHistory.setEventId(eventsHistoryDto.getEventId());
        eventsHistory.setUserId(eventsHistoryDto.getUserId());

        return eventsHistory;
    }


    public static EventsHistoryDto mapToEventsHistoryDto( EventsHistory eventsHistory, EventsHistoryDto eventsHistoryDto) {

        eventsHistoryDto.setHistoryType(eventsHistory.getHistoryType());
        eventsHistoryDto.setUserId(eventsHistory.getUserId());
        eventsHistoryDto.setEventId(eventsHistory.getEventId());

        return eventsHistoryDto;
    }
}
