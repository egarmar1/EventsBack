package com.kike.events.service.impl;

import com.kike.events.constants.Availability;
import com.kike.events.dto.EventCreateDto;
import com.kike.events.dto.EventResponseDto;
import com.kike.events.dto.EventUpdateDto;
import com.kike.events.dto.client.EventsHistoryDto;
import com.kike.events.dto.client.UserTypeDto;
import com.kike.events.entity.Event;
import com.kike.events.exception.CurrentBookingsGreaterThanMaxException;
import com.kike.events.exception.ForbiddenException;
import com.kike.events.exception.IncorrectTypeOfUserException;
import com.kike.events.exception.ResourceNotFoundException;
import com.kike.events.repository.EventRepository;
import com.kike.events.service.auth.JwtService;
import com.kike.events.service.client.UserFeignClient;
import org.hibernate.usertype.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceImplTest {

    private static final String TITLE = "Bachata Class in center of Valencia";
    private static final String DESCRIPTION = "Bachata class in Valencia, on October 25th, no min level";
    private static final BigDecimal PRICE = new BigDecimal("10.9");
    private static final Availability AVAILABILITY = Availability.AVAILABLE;
    private static final long MAX_BOOKINGS = 50L;
    private static final long CURRENT_BOOKINGS = 10L;
    private static final long EVENT_ID = 1L;


    @Mock
    private EventRepository eventRepository;

    @Mock
    private StreamBridge streamBridge;

    @Mock
    private UserFeignClient userFeignClient;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private EventServiceImpl eventServiceImpl;

    private Jwt jwt;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwt = mock(Jwt.class);
    }

    @Test
    @DisplayName("Given a user of type vendor WHEN creates an event THEN an event is created")
    void shouldCreateEventWhenVendorTypeIsCorrect() {
        // Given
        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        EventCreateDto eventCreateDto = buildEventCreateDto(vendorId);
        Event savedEvent = buildEvent(vendorId);
        EventResponseDto expectedResponseDto = buildEventResponseDto(vendorId);
        when(userFeignClient.getType(vendorId)).thenReturn(new ResponseEntity<>(new UserTypeDto("vendor"), HttpStatus.OK));
        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);
        when(jwtService.getRealmRoles(jwt)).thenReturn(Collections.emptyList());
        when(jwt.getClaim("sub")).thenReturn(vendorId);

        // When
        EventResponseDto eventResponseDto = eventServiceImpl.createEvent(eventCreateDto, jwt);

        // Then
        verify(eventRepository).save(any(Event.class));
        verify(streamBridge).send(eq("create-event-history"), any(EventsHistoryDto.class));
        assertEquals(eventResponseDto, expectedResponseDto);

    }

    @Test
    @DisplayName("Given a user of that is not type vendor WHEN creates an event THEN IncorrectTypeOfUserException is thrown")
    void shouldThrowIncorrectTypeOfUserExceptionWhenUserIsNotVendor() {

        // Given
        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        EventCreateDto eventCreateDto = buildEventCreateDto(vendorId);
        when(userFeignClient.getType(vendorId)).thenReturn(new ResponseEntity<>(new UserTypeDto("client"), HttpStatus.OK));
        when(jwtService.getRealmRoles(jwt)).thenReturn(Collections.emptyList());
        when(jwt.getClaim("sub")).thenReturn(vendorId);

        // When / Then
        assertThrows(IncorrectTypeOfUserException.class, () -> eventServiceImpl.createEvent(eventCreateDto, jwt));

    }

    @Test
    @DisplayName("Given an admin user WHEN creates an event THEN event is created")
    void shouldCreateEventWhenUserIsAdmin() {

        // Given
        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        EventCreateDto eventCreateDto = buildEventCreateDto(vendorId);
        Event savedEvent = buildEvent(vendorId);
        EventResponseDto expectedResponseDto = buildEventResponseDto(vendorId);
        when(userFeignClient.getType(vendorId)).thenReturn(new ResponseEntity<>(new UserTypeDto("client"), HttpStatus.OK));
        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);
        when(jwtService.getRealmRoles(jwt)).thenReturn(List.of("admin"));

        // When
        EventResponseDto eventResponseDto = eventServiceImpl.createEvent(eventCreateDto, jwt);

        // Then
        verify(eventRepository).save(any(Event.class));
        verify(streamBridge).send(eq("create-event-history"), any(EventsHistoryDto.class));
        assertEquals(eventResponseDto, expectedResponseDto);

    }

    // Fetch
    @Test
    @DisplayName("Given a vendor who owns the event WHEN fetches the event THEN the info of the event is returned")
    void shouldFetchEventWhenProperVendor() {

        // Given
        Long eventId = 1L;
        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        Event event = buildEvent(vendorId);
        EventResponseDto expectedResponseDto = buildEventResponseDto(vendorId);
        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        when(jwtService.getRealmRoles(jwt)).thenReturn(Collections.emptyList());
        when(jwt.getClaim("sub")).thenReturn(vendorId);

        // When
        EventResponseDto resultResponseDto = eventServiceImpl.fetchEvent(eventId, jwt);

        // Then
        assertEquals(expectedResponseDto, resultResponseDto);
        verify(eventRepository).findById(eventId);
        verify(jwtService).getRealmRoles(jwt);

    }

    @Test
    @DisplayName("Given an admin WHEN fetches the event THEN the info of the event is returned")
    void fetchEventAsAdminSuccessful() {

        // Given
        Long eventId = 1L;
        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        String otherVendorId = "d31a4755-e357-4c18-07e9-b54da83dd4da";
        Event event = buildEvent(vendorId);
        EventResponseDto expectedResponseDto = buildEventResponseDto(vendorId);
        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        when(jwtService.getRealmRoles(jwt)).thenReturn(List.of("admin"));
        when(jwt.getClaim("sub")).thenReturn(otherVendorId);

        // When
        EventResponseDto resultResponseDto = eventServiceImpl.fetchEvent(eventId, jwt);

        // Then
        assertEquals(expectedResponseDto, resultResponseDto);
        verify(eventRepository).findById(eventId);
        verify(jwtService).getRealmRoles(jwt);

    }

    @Test
    @DisplayName("Given a vendor who does not own the event WHEN attempting to fetch the event THEN a ForbiddenException is thrown")
    void fetchEventOfDifferentVendorThrowsForbiddenException() {

        // Given
        Long eventId = 1L;
        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        String otherVendorId = "d31a4755-e357-4c18-07e9-b54da83dd4da";
        Event event = buildEvent(vendorId);

        when(eventRepository.findById(any())).thenReturn(Optional.of(event));
        when(jwtService.getRealmRoles(jwt)).thenReturn(Collections.emptyList());
        when(jwt.getClaim("sub")).thenReturn(otherVendorId);

        // When / Then
        assertThrows(ForbiddenException.class, () -> eventServiceImpl.fetchEvent(eventId, jwt));

    }

    @Test
    @DisplayName("Given a event id that does not exist WHEN attempting to fetch the event THEN a ResourceNotFoundException is thrown")
    void shouldThrowResourceNotFoundExceptionWhenNotProperEvent() {
        // Given
        Long eventId = 1L;
        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        when(eventRepository.findById(any())).thenReturn(Optional.empty());
        when(jwtService.getRealmRoles(jwt)).thenReturn(Collections.emptyList());
        when(jwt.getClaim("sub")).thenReturn(vendorId);

        // When / Then
        assertThrows(ResourceNotFoundException.class, () -> eventServiceImpl.fetchEvent(eventId, jwt));

    }

    @Test
    @DisplayName("Given a vendor that owns the event WHEN to updates the event THEN the event is updated")
    void updateEventAsOwnerUpdatesEventSuccessfully() {

        // Given
        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        EventUpdateDto eventUpdateDto = buildEventUpdateDto(vendorId);
        when(jwt.getClaim("sub")).thenReturn(vendorId);
        when(jwtService.getRealmRoles(jwt)).thenReturn(Collections.emptyList());
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(buildEvent(vendorId)));


        // When
        boolean isUpdated = eventServiceImpl.updateEvent(eventUpdateDto, jwt);

        // Then
        verify(eventRepository).findById(any());
        verify(jwt, atLeast(1)).getClaim("sub");
        assertTrue(isUpdated);

    }

    @Test
    @DisplayName("Given a vendor that does not own the event WHEN tries to update the event THEN the ForbiddenException is thrown")
    void updateEventAsNotOwnerThrowsForbiddenException() {
        // Given
        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        String otherVendorId = "d31a4755-e357-4c18-07e9-b54da83dd4da";
        EventUpdateDto eventUpdateDto = buildEventUpdateDto(vendorId);
        when(jwt.getClaim("sub")).thenReturn(vendorId);
        when(jwtService.getRealmRoles(jwt)).thenReturn(Collections.emptyList());
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(buildEvent(otherVendorId)));

        // When / Then
        ForbiddenException forbiddenException = assertThrows(ForbiddenException.class, () -> eventServiceImpl.updateEvent(eventUpdateDto, jwt));
        assertTrue(forbiddenException.getMessage().contains("the event with id:"));

    }

    @Test
    @DisplayName("Given a vendor that owns the event WHEN tries to update vendorId THEN the ForbiddenException is thrown")
    void updateEventAsOwnerAndChangeVendorIdThrowsForbiddenException() {
        // Given
        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        String otherVendorId = "d31a4755-e357-4c18-07e9-b54da83dd4da";

        EventUpdateDto eventUpdateDto = buildEventUpdateDto(otherVendorId);
        when(jwt.getClaim("sub")).thenReturn(vendorId);
        when(jwtService.getRealmRoles(jwt)).thenReturn(Collections.emptyList());
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(buildEvent(vendorId)));

        // When / Then
        ForbiddenException forbiddenException = assertThrows(ForbiddenException.class, () -> eventServiceImpl.updateEvent(eventUpdateDto, jwt));
        assertTrue(forbiddenException.getMessage().contains("not allowed to change the vendor"));


    }


    @Test
    @DisplayName("Given a vendor WHEN tries to update the maxNumBookings with a value lower that the currentNumBookings THEN CurrentBookingsGreaterThanMaxException is thrown")
    void updateEventWithMaxBookingsLowerThanCurrentBookingsThrowsCurrentBookingsGreaterThanMaxException() {
        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";

        EventUpdateDto eventUpdateDto = new EventUpdateDto(EVENT_ID,
                TITLE,
                DESCRIPTION,
                PRICE,
                vendorId,
                AVAILABILITY,
                CURRENT_BOOKINGS - 1);
        when(jwt.getClaim("sub")).thenReturn(vendorId);
        when(jwtService.getRealmRoles(jwt)).thenReturn(Collections.emptyList());
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(buildEvent(vendorId)));

        // When / Then
        assertThrows(CurrentBookingsGreaterThanMaxException.class, () -> eventServiceImpl.updateEvent(eventUpdateDto, jwt));

    }

    @Test
    @DisplayName("Given a vendor that owns the event WHEN tries to delete the event THEN the event deletes successfully")
    void deleteEventAsOwnerDeletesEventSuccessfully() {

        // Given
        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        when(jwt.getClaim("sub")).thenReturn(vendorId);
        when(jwtService.getRealmRoles(jwt)).thenReturn(Collections.emptyList());
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(buildEvent(vendorId)));


        // When
        eventServiceImpl.deleteEvent(EVENT_ID, jwt);

        // Then
        verify(eventRepository).findById(any());
        verify(jwt, atLeast(1)).getClaim("sub");
        verify(eventRepository,times(1)).deleteById(any());

    }

    @Test
    @DisplayName("Given an incorrect eventId WHEN deletion of the event is tried THEN ResourceNotFoundException is thrown")
    void deleteEventWithIncorrectEventIdThrowsResourceNotFoundException() {

        // Given
        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        long otherEventId = 2L;
        when(jwt.getClaim("sub")).thenReturn(vendorId);
        when(jwtService.getRealmRoles(jwt)).thenReturn(Collections.emptyList());
        when(eventRepository.findById(otherEventId)).thenReturn(Optional.empty());


        // When / Then

        assertThrows(ResourceNotFoundException.class, () -> eventServiceImpl.deleteEvent(otherEventId, jwt));

    }

    @Test
    @DisplayName("Given a vendor that does not own the event WHEN tries to delete the event THEN ForbiddenException is thrown")
    void deleteEventWithDifferentOwnerThrowsForbiddenException() {

        // Given
        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        String otherVendorId = "d31a4755-e357-4c18-07e9-b54da83dd4da";

        when(jwt.getClaim("sub")).thenReturn(vendorId);
        when(jwtService.getRealmRoles(jwt)).thenReturn(Collections.emptyList());
        when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(buildEvent(otherVendorId)));

        // When / Then

        assertThrows(ForbiddenException.class, () -> eventServiceImpl.deleteEvent(EVENT_ID, jwt));

    }

    @Test
    @DisplayName("Given an admin WHEN tries to delete the event THEN the event deletes successfully")
    void deleteEventAsAdminDeletesEventSuccessfully() {

        // Given
        String adminId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        String otherVendorId = "d31a4755-e357-4c18-07e9-b54da83dd4da";

        when(jwt.getClaim("sub")).thenReturn(adminId);
        when(jwtService.getRealmRoles(jwt)).thenReturn(List.of("admin"));
        when(eventRepository.findById(any(Long.class))).thenReturn(Optional.of(buildEvent(otherVendorId)));


        // When
        eventServiceImpl.deleteEvent(EVENT_ID, jwt);

        // Then
        verify(eventRepository).findById(any());
        verify(jwt, atLeast(1)).getClaim("sub");
        verify(eventRepository,times(1)).deleteById(any());

    }

    private Event buildEvent(String vendorId) {
        return new Event(EVENT_ID, TITLE, DESCRIPTION, PRICE, vendorId, AVAILABILITY, MAX_BOOKINGS, CURRENT_BOOKINGS);
    }

    private EventResponseDto buildEventResponseDto(String vendorId) {
        return new EventResponseDto(EVENT_ID, TITLE, DESCRIPTION, PRICE, vendorId, AVAILABILITY, MAX_BOOKINGS, CURRENT_BOOKINGS);
    }

    private EventCreateDto buildEventCreateDto(String vendorId) {
        return new EventCreateDto(TITLE, DESCRIPTION, PRICE, vendorId, AVAILABILITY, MAX_BOOKINGS);
    }

    private EventUpdateDto buildEventUpdateDto(String vendorId) {
        return new EventUpdateDto(EVENT_ID, TITLE, DESCRIPTION, PRICE, vendorId, AVAILABILITY, MAX_BOOKINGS);

    }

}