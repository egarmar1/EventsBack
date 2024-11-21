package com.kike.eventReviews.controller;

import com.kike.eventReviews.constants.EventReviewsConstants;
import com.kike.eventReviews.dto.EventReviewsCreateDto;
import com.kike.eventReviews.dto.ResponseDto;
import com.kike.eventReviews.service.IEventReviewsService;
import feign.Body;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.kike.eventReviews.constants.EventReviewsConstants.*;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class EventReviewsController {

    IEventReviewsService eventReviewsService;


    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createEventReview(@RequestBody EventReviewsCreateDto eventReviewsCreateDto,
                                                         @AuthenticationPrincipal Jwt jwt) {

        eventReviewsService.createEventReview(eventReviewsCreateDto,jwt);

        return ResponseEntity.status(200).body(new ResponseDto(STATUS_200, MESSAGE_200));

    }
}
