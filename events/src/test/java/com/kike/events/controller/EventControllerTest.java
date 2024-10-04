package com.kike.events.controller;

import com.kike.events.constants.Availability;
import com.kike.events.constants.EventConstants;
import com.kike.events.dto.EventCreateDto;
import com.kike.events.dto.EventResponseDto;
import com.kike.events.dto.EventUpdateDto;
import com.kike.events.dto.ResponseDto;
import com.kike.events.service.IEventService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Objects;

import static com.kike.events.constants.EventConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventControllerTest {


    @Mock
    IEventService iEventService;

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

        Long id = 1412L;
        String title = "Bachata Class in center of Valencia";
        String description = "Bachata Class in center of Valencia, on day 25 of october, no minimum level required ";
        BigDecimal price = new BigDecimal("10.9");
        int vendorId = 154;
        Availability availability = Availability.AVAILABLE;
        Long maxNumBookings = 154L;

        EventCreateDto eventCreateDto = new EventCreateDto(title, description, price, vendorId, availability, maxNumBookings);
        EventResponseDto eventResponseDto = new EventResponseDto(id, title, description, price, vendorId, availability,maxNumBookings, 0L);

        when(iEventService.createEvent(eventCreateDto)).thenReturn(eventResponseDto);

        ResponseEntity<EventResponseDto> response = eventController.createEvent(eventCreateDto);

        verify(iEventService, times(1)).createEvent(eventCreateDto);

        assertEquals(response.getBody(), eventResponseDto);
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
    }

    @Test
    @DisplayName("GIVEN an existing ID WHEN the fetchEvent method from the event controller is called THEN an event is return")
    void fetchEventTest() {

        Long id = 1412L;
        String title = "Bachata Class in center of Valencia";
        String description = "Bachata Class in center of Valencia, on day 25 of october, no minimum level required ";
        BigDecimal price = new BigDecimal("10.9");
        int vendorId = 154;
        Availability availability = Availability.AVAILABLE;
        Long maxNumBookings = 154L;

        EventResponseDto eventResponseDto = new EventResponseDto(id, title, description, price, vendorId, availability,maxNumBookings, 0L);

        when(iEventService.fetchEvent(id)).thenReturn(eventResponseDto);

        ResponseEntity<EventResponseDto> response = eventController.fetchEvent(id);

        verify(iEventService, times(1)).fetchEvent(id);

        assertEquals(response.getBody(), eventResponseDto);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    @DisplayName("GIVEN an event information of an existing event WHEN the updateEvent method from the event controller is called THEN the event is updated with the new provided info")
    void updateEventTest() {

        Long id = 1412L;
        String title = "Bachata Class in center of Valencia";
        String description = "Bachata Class in center of Valencia, on day 25 of october, no minimum level required ";
        BigDecimal price = new BigDecimal("10.9");
        int vendorId = 154;
        Availability availability = Availability.AVAILABLE;
        Long maxNumBookings = 154L;

        EventUpdateDto eventResponseDto = new EventUpdateDto(id, title, description, price, vendorId, availability, maxNumBookings);

        when(iEventService.updateEvent(eventResponseDto)).thenReturn(true);

        ResponseEntity<ResponseDto> response = eventController.updateEvent(eventResponseDto);

        verify(iEventService, times(1)).updateEvent(eventResponseDto);

        assertEquals(Objects.requireNonNull(response.getBody()).getStatusCode(), STATUS_200);
        assertEquals(response.getBody().getStatusMsg(), MESSAGE_200);
    }

    @Test
    @DisplayName("GIVEN an event information of an existing event WHEN the deleteEvent method from the event controller is called THEN the event is deleted")
    void deleteEventTest() {

        Long id = 1412L;

        ResponseEntity<ResponseDto> response = eventController.deleteEvent(id);

        verify(iEventService, times(1)).deleteEvent(id);

        assertEquals(Objects.requireNonNull(response.getBody()).getStatusCode(), STATUS_200);
        assertEquals(response.getBody().getStatusMsg(), MESSAGE_200);
    }
}