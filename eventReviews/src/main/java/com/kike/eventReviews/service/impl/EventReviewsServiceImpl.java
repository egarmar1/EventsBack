package com.kike.eventReviews.service.impl;

import com.kike.eventReviews.constants.HistoryType;
import com.kike.eventReviews.dto.EventReviewsCreateDto;
import com.kike.eventReviews.dto.EventReviewsResponseDto;
import com.kike.eventReviews.dto.EventsHistoryDto;
import com.kike.eventReviews.repository.EventReviewsRepository;
import com.kike.eventReviews.service.IEventReviewsService;
import com.kike.eventReviews.service.client.EventsHistoryFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EventReviewsServiceImpl implements IEventReviewsService {

    private EventReviewsRepository eventReviewsRepository;
    private EventsHistoryFeignClient eventsHistoryFeignClient;

    @Override
    public void createEventReview(EventReviewsCreateDto eventReviewsCreateDto, Jwt jwt) {

        EventsHistoryDto eventsHistoryDto = eventsHistoryFeignClient.getEventHistoryByUserIdAndEventId(eventReviewsCreateDto.getUserId(),
                eventReviewsCreateDto.getEventId(),
                jwt).getBody();


        if(!eventsHistoryDto.getHistoryType().equals(HistoryType.ATTENDED_EVENT)){
            throw new RuntimeException("");
        }


    }

    @Override
    public EventReviewsResponseDto fetchEventReview(String userId, Long eventId, Jwt jwt) {
        return null;
    }

    @Override
    public void updateEventReview(EventReviewsCreateDto eventReviewsCreateDto, Jwt jwt) {

    }

    @Override
    public void deleteEventReview(String userId, Long eventId, Jwt jwt) {

    }
}
