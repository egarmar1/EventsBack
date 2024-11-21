package com.kike.eventReviews.service;


import com.kike.eventReviews.dto.EventReviewsCreateDto;
import com.kike.eventReviews.dto.EventReviewsResponseDto;
import org.springframework.security.oauth2.jwt.Jwt;

public interface IEventReviewsService {


    void createEventReview(EventReviewsCreateDto eventReviewsCreateDto, Jwt jwt);

    EventReviewsResponseDto fetchEventReview(String userId, Long eventId, Jwt jwt);

    void updateEventReview(EventReviewsCreateDto eventReviewsCreateDto, Jwt jwt);

    void deleteEventReview(String userId, Long eventId, Jwt jwt);

}
