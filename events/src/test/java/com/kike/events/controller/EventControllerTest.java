package com.kike.events.controller;

import com.kike.events.constants.Availability;
import com.kike.events.constants.EventConstants;
import com.kike.events.dto.EventCreateDto;
import com.kike.events.dto.EventResponseDto;
import com.kike.events.dto.EventUpdateDto;
import com.kike.events.dto.ResponseDto;
import com.kike.events.entity.Event;
import com.kike.events.service.IEventService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

import static com.kike.events.constants.EventConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventControllerTest {

    private static final String TITLE = "Bachata Class in center of Valencia";
    private static final String DESCRIPTION = "Bachata class in Valencia, on October 25th, no min level";
    private static final BigDecimal PRICE = new BigDecimal("10.9");
    private static final Availability AVAILABILITY = Availability.AVAILABLE;
    private static final long MAX_BOOKINGS = 50L;
    private static final long CURRENT_BOOKINGS = 10L;
    private static final long EVENT_ID = 1L;

    @Mock
    IEventService iEventService;

    @Mock
    StreamBridge streamBridge;

    @InjectMocks
    EventController eventController;

    private Validator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("GIVEN an event information WHEN the createEvent method from the event controller is called THEN an event is created with the provided information")
    void createEventTest() {

        Jwt jwt = mock(Jwt.class);
        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";

        EventCreateDto eventCreateDto = buildEventCreateDto(vendorId);
        EventResponseDto eventResponseDto = buildEventResponseDto(vendorId);

        when(iEventService.createEvent(eventCreateDto, jwt)).thenReturn(eventResponseDto);

        ResponseEntity<EventResponseDto> response = eventController.createEvent(eventCreateDto, jwt);

        verify(iEventService, times(1)).createEvent(eventCreateDto, jwt);

        assertEquals(response.getBody(), eventResponseDto);
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
    }

    @Test
    @DisplayName("GIVEN an invalid event information WHEN createEvent method is called THEN a BAD REQUEST response is returned")
    void createEventWithIncorrectInfoReturnBadRequestTest() {
        Jwt jwt = mock(Jwt.class);

        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        EventCreateDto invalidEventDto = new EventCreateDto("", DESCRIPTION, PRICE, vendorId, AVAILABILITY, MAX_BOOKINGS);

        // Act
        // Ejecutar el método del controlador directamente
        ResponseEntity<EventResponseDto> response = eventController.createEvent(invalidEventDto, jwt);

        // Assert
        // Comprobar que el estado de respuesta es 400 BAD REQUEST
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("GIVEN an incorrect title event information WHEN is validated THEN a violation is triggered")
    void createEventWithIncorrectTitleContainsViolationTest() {

        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";

        EventCreateDto eventCreateDto = new EventCreateDto("", DESCRIPTION, PRICE, vendorId, AVAILABILITY, MAX_BOOKINGS);


        Set<ConstraintViolation<EventCreateDto>> violations = validator.validate(eventCreateDto);

        assertFalse(violations.isEmpty(), "Expected a validation error due to empty title");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("title")),
                "expected validation error on title field");
    }

    @Test
    @DisplayName("GIVEN an existing ID WHEN the fetchEvent method from the event controller is called THEN an event is return")
    void fetchEventTest() {

        Jwt jwt = mock(Jwt.class);
        Long id = 1412L;
        String title = "Bachata Class in center of Valencia";
        String description = "Bachata Class in center of Valencia, on day 25 of october, no minimum level required ";
        BigDecimal price = new BigDecimal("10.9");
        String vendorId = "154";
        Availability availability = Availability.AVAILABLE;
        Long maxNumBookings = 154L;

        EventResponseDto eventResponseDto = new EventResponseDto(id, title, description, price, vendorId, availability, maxNumBookings, 0L);

        when(iEventService.fetchEvent(id, jwt)).thenReturn(eventResponseDto);

        ResponseEntity<EventResponseDto> response = eventController.fetchEvent(id, jwt);

        verify(iEventService, times(1)).fetchEvent(id, jwt);

        assertEquals(response.getBody(), eventResponseDto);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    @DisplayName("GIVEN an event information of an existing event WHEN the updateEvent method from the event controller is called THEN the event is updated with the new provided info")
    void updateEventTest() {

        Long id = 1412L;
        Jwt jwt = mock(Jwt.class);

        String title = "Bachata Class in center of Valencia";
        String description = "Bachata Class in center of Valencia, on day 25 of october, no minimum level required ";
        BigDecimal price = new BigDecimal("10.9");
        String vendorId = "154";
        Availability availability = Availability.AVAILABLE;
        Long maxNumBookings = 154L;

        EventUpdateDto eventResponseDto = new EventUpdateDto(id, title, description, price, vendorId, availability, maxNumBookings);

        when(iEventService.updateEvent(eventResponseDto, jwt)).thenReturn(true);
        ResponseEntity<ResponseDto> response = eventController.updateEvent(eventResponseDto, jwt);

        verify(iEventService, times(1)).updateEvent(eventResponseDto, jwt);

        assertEquals(Objects.requireNonNull(response.getBody()).getStatusCode(), STATUS_200);
        assertEquals(response.getBody().getStatusMsg(), MESSAGE_200);
        int[][]ia,iaa;
    }

    @Test
    @DisplayName("GIVEN an event information of an existing event WHEN the deleteEvent method from the event controller is called THEN the event is deleted")
    void deleteEventTest() {

        Long id = 1412L;
        Jwt jwt = mock(Jwt.class);

        ResponseEntity<ResponseDto> response = eventController.deleteEvent(id, jwt);

        verify(iEventService, times(1)).deleteEvent(id, jwt);

        assertEquals(Objects.requireNonNull(response.getBody()).getStatusCode(), STATUS_200);
        assertEquals(response.getBody().getStatusMsg(), MESSAGE_200);
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