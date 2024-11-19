package com.kike.events.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kike.events.constants.Availability;
import com.kike.events.dto.EventCreateDto;
import com.kike.events.dto.EventResponseDto;
import com.kike.events.dto.EventUpdateDto;
import com.kike.events.exception.ForbiddenException;
import com.kike.events.exception.IncorrectTypeOfUserException;
import com.kike.events.exception.ResourceNotFoundException;
import com.kike.events.service.IEventService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class EventControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;
    private Jwt jwt;

    @MockBean
    private IEventService eventService;
    @MockBean
    private StreamBridge streamBridge;

    @InjectMocks
    private EventController eventController;

    private static final Logger log = LoggerFactory.getLogger(EventControllerMvcTest.class);

    private static final String TITLE = "Bachata Class in center of Valencia";
    private static final String DESCRIPTION = "Bachata class in Valencia, on October 25th, no min level";
    private static final BigDecimal PRICE = new BigDecimal("10.9");
    private static final Availability AVAILABILITY = Availability.AVAILABLE;
    private static final long MAX_BOOKINGS = 50L;
    private static final long CURRENT_BOOKINGS = 10L;
    private static final long EVENT_ID = 1L;


    @BeforeEach
    void setUp(WebApplicationContext context) {

        jwt = new Jwt(
                "sample-token-value",
                Instant.now(),
                Instant.now().plus(Duration.ofHours(1)),
                Map.of("alg", "RS256"),
                Map.of("sub", "user-id")
        );

        // Configurar el SecurityContext con el Jwt
        SecurityContextHolder.getContext().setAuthentication(
                new JwtAuthenticationToken(jwt)
        );

        // Configurar MockMvc con el controlador y el contexto de seguridad
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        log.info("Starting setUp");
    }

    @Test
    @DisplayName("GIVEN a vendor with the information to create an event WHEN the create event endpoint is called THEN the event is created in the database with the data indicated")
    void createEventSuccessful() throws Exception {

        //Given
        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        EventCreateDto eventCreateDto = buildEventCreateDto(vendorId);
        EventResponseDto newEventDto = new EventResponseDto(EVENT_ID, TITLE, DESCRIPTION, PRICE, vendorId, AVAILABILITY, MAX_BOOKINGS, 0L);


        when(eventService.createEvent(any(EventCreateDto.class), any(Jwt.class))).thenReturn(newEventDto);

        //When

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(eventCreateDto))
                        .characterEncoding("utf-8"))
                // Then
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(newEventDto.getId()))
                .andExpect(jsonPath("$.currentNumBookings").value(0));

    }

    @Test
    @DisplayName("GIVEN a vendor with incorrect information to create an event WHEN the create event endpoint is called THEN a BAD REQUEST status is returned")
    void createEventBadRequest() throws Exception {

        //Given
        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        EventCreateDto wrongEventCreateDto = new EventCreateDto("", DESCRIPTION, PRICE, vendorId, AVAILABILITY, MAX_BOOKINGS);

        //When
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(wrongEventCreateDto))
                        .characterEncoding("utf-8"))
                // Then
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"));


    }

    @Test
    @DisplayName("GIVEN a server error occurs WHEN the create event endpoint is called THEN an INTERNAL SERVER ERROR is returned")
    void createEventInternalServerError() throws Exception {

        //Given
        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        EventCreateDto wrongEventCreateDto = new EventCreateDto(TITLE, DESCRIPTION, PRICE, vendorId, AVAILABILITY, MAX_BOOKINGS);

        when(eventService.createEvent(any(EventCreateDto.class), any(Jwt.class)))
                .thenThrow(new RuntimeException("Error"));


        //When
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(wrongEventCreateDto))
                        .characterEncoding("utf-8"))
                // Then
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value("INTERNAL_SERVER_ERROR"));


    }

    @Test
    @DisplayName("GIVEN a vendor with an event that contains another vendorId WHEN the create event endpoint is called THEN a FORBIDDEN status is returned")
    void createEventWithAnotherVendorIdForbidden() throws Exception {

        //Given
        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        EventCreateDto wrongEventCreateDto = new EventCreateDto(TITLE, DESCRIPTION, PRICE, vendorId, AVAILABILITY, MAX_BOOKINGS);

        when(eventService.createEvent(any(EventCreateDto.class), any(Jwt.class)))
                .thenThrow(new ForbiddenException("You are not allowed to create the event " +
                        " for vendor with id: " + vendorId));


        //When
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(wrongEventCreateDto))
                        .characterEncoding("utf-8"))
                // Then
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value("FORBIDDEN"));


    }

    @Test
    @DisplayName("GIVEN a non-vendor user WHEN the create event endpoint is called THEN a FORBIDDEN status is returned")
    void createEventWithIncorrectTypeOfUserReturnsForbidden() throws Exception {

        //Given
        String clientId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        EventCreateDto wrongEventCreateDto = new EventCreateDto(TITLE, DESCRIPTION, PRICE, clientId, AVAILABILITY, MAX_BOOKINGS);

        when(eventService.createEvent(any(EventCreateDto.class), any(Jwt.class)))
                .thenThrow(new IncorrectTypeOfUserException(clientId, "vendor", "client"));


        //When
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(wrongEventCreateDto))
                        .characterEncoding("utf-8"))
                // Then
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value("FORBIDDEN"));
    }

    @Test
    @DisplayName("GIVEN a valid eventId WHEN the fetch event endpoint is called THEN the event is returned")
    void fetchEventSuccessful() throws Exception {

        //Given
        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        long eventId = 33L;
        EventResponseDto eventResponseDto = buildEventResponseDto(vendorId);

        when(eventService.fetchEvent(any(Long.class), any(Jwt.class))).thenReturn(eventResponseDto);

        //When
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/fetch?id=" + eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                // Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(eventResponseDto.getId()))
                .andExpect(jsonPath("$.currentNumBookings").value(eventResponseDto.getCurrentNumBookings()))
                .andExpect(jsonPath("$.vendorId").value(vendorId))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("GIVEN an eventId with characters WHEN the fetch event endpoint is called THEN BAD REQUEST is returned")
    void fetchEventBadRequest() throws Exception {

        //Given
        String wrongEventId = "23fa3";

        //When
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/fetch?id=" + wrongEventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                // Then
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    @DisplayName("GIVEN a non existing eventId  WHEN the fetch event endpoint is called THEN NOT FOUND is returned")
    void fetchEventNotFound() throws Exception {

        //Given
        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        long nonExistingEventId = 33L;

        when(eventService.fetchEvent(any(Long.class), any(Jwt.class))).thenThrow(new ResourceNotFoundException("Event", "id", Long.toString(nonExistingEventId)));

        //When
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/fetch?id=" + nonExistingEventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                // Then
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"));

    }

    @Test
    @DisplayName("GIVEN an internal server error WHEN the fetch event endpoint is called THEN INTERNAL SERVER ERROR is returned")
    void fetchEventInternalServerError() throws Exception {

        //Given
        long eventId = 33L;

        when(eventService.fetchEvent(any(Long.class), any(Jwt.class))).thenThrow(new RuntimeException());

        //When
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/fetch?id=" + eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                // Then
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value("INTERNAL_SERVER_ERROR"));

    }

    @Test
    @DisplayName("GIVEN a vendor WHEN tries to fetch an event that does not own THEN FORBIDDEN is returned")
    void fetchEventForbidden() throws Exception {

        //Given
        long eventId = 33L;

        when(eventService.fetchEvent(any(Long.class), any(Jwt.class)))
                .thenThrow(new ForbiddenException("You don't have permissions for info about event with id  " + eventId));

        //When
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/fetch?id=" + eventId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                // Then
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value("FORBIDDEN"));

    }

    @Test
    @DisplayName("GIVEN a vendor with correct updateEventDto WHEN updates an event that owns THEN the event is updated")
    void updateEventSuccessful() throws Exception {
        //Given
        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        EventUpdateDto eventUpdateDto = buildEventUpdateDto(vendorId);


        when(eventService.updateEvent(any(EventUpdateDto.class), any(Jwt.class))).thenReturn(true);

        //When

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(eventUpdateDto))
                        .characterEncoding("utf-8"))
                // Then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusMsg").value("Request processed successfully"));
    }

    @Test
    @DisplayName("GIVEN a vendor with incorrect updateEventDto WHEN tries to update an event THEN BAD REQUEST is returned")
    void updateEventBadRequest() throws Exception {
        //Given
        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        EventUpdateDto eventUpdateDto = new EventUpdateDto(EVENT_ID, "", DESCRIPTION, PRICE, vendorId, AVAILABILITY, -1L);


        when(eventService.updateEvent(any(EventUpdateDto.class), any(Jwt.class))).thenReturn(true);

        //When

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(eventUpdateDto))
                        .characterEncoding("utf-8"))
                // Then
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value(Matchers.containsString("with 2 errors")));
    }

    @Test
    @DisplayName("GIVEN a non existing eventId WHEN update event method is called THEN NOT FOUND is returned")
    void updateEventNotFound() throws Exception {
        //Given
        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        EventUpdateDto eventUpdateDto = new EventUpdateDto(EVENT_ID, TITLE, DESCRIPTION, PRICE, vendorId, AVAILABILITY, MAX_BOOKINGS);


        when(eventService.updateEvent(any(EventUpdateDto.class), any(Jwt.class)))
                .thenThrow(new ResourceNotFoundException("Event", "id", Long.toString(EVENT_ID)));

        //When
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(eventUpdateDto))
                        .characterEncoding("utf-8"))
                // Then
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"))
                .andExpect(jsonPath("$.errorMessage").value(Matchers.containsString("Event not found")));
    }

    @Test
    @DisplayName("GIVEN an internal error WHEN update event method is called THEN INTERNAL SERVER ERROR is returned")
    void updateEventInternalServerError() throws Exception {
        //Given
        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        EventUpdateDto eventUpdateDto = new EventUpdateDto(EVENT_ID, TITLE, DESCRIPTION, PRICE, vendorId, AVAILABILITY, MAX_BOOKINGS);


        when(eventService.updateEvent(any(EventUpdateDto.class), any(Jwt.class)))
                .thenThrow(new RuntimeException());

        //When
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(eventUpdateDto))
                        .characterEncoding("utf-8"))
                // Then
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value("INTERNAL_SERVER_ERROR"));
    }

    @Test
    @DisplayName("GIVEN a vendor WHEN tries to update an event that does not own THEN FORBIDDEN is returned")
    void updateEventForbidden() throws Exception {
        //Given
        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        EventUpdateDto eventUpdateDto = new EventUpdateDto(EVENT_ID, TITLE, DESCRIPTION, PRICE, vendorId, AVAILABILITY, MAX_BOOKINGS);


        when(eventService.updateEvent(any(EventUpdateDto.class), any(Jwt.class)))
                .thenThrow(new ForbiddenException("You are not allowed to change" +
                        " the event with id: " + eventUpdateDto.getId()));

        //When
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(eventUpdateDto))
                        .characterEncoding("utf-8"))
                // Then
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value("FORBIDDEN"));
    }

    @Test
    @DisplayName("GIVEN a vendor WHEN deletes an event that owns THEN the event is deleted")
    void deleteEventSuccessful() throws Exception {
        //Given
        long eventId = 22L;

        doNothing().when(eventService).deleteEvent(any(Long.class), any(Jwt.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/delete?id=" + eventId)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusMsg").value("Request processed successfully"));

    }

    @Test
    @DisplayName("GIVEN an invalid eventId format WHEN attempting to delete an event THEN BAD REQUEST is returned")
    void deleteEventBadRequest() throws Exception {
        //Given
        String incorrectEventId = "22fs";

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/delete?id=" + incorrectEventId))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GIVEN a non existing eventId WHEN attempting to delete an event THEN NOT FOUND is returned")
    void deleteEventNotFound() throws Exception {
        long eventId = 22L;

        doThrow(new ResourceNotFoundException("Event", "id", Long.toString(eventId)))
                .when(eventService).deleteEvent(any(Long.class), any(Jwt.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/delete?id=" + eventId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"));
    }

    @Test
    @DisplayName("GIVEN a server error occurs WHEN attempting to delete an event THEN INTERNAL SERVER ERROR is returned")
    void deleteEventInternalServerError() throws Exception {
        long eventId = 22L;

        doThrow(new RuntimeException())
                .when(eventService).deleteEvent(any(Long.class), any(Jwt.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/delete?id=" + eventId))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errorCode").value("INTERNAL_SERVER_ERROR"));
    }

    @Test
    @DisplayName("GIVEN a vendor WHEN attempting to delete an event that does not own THEN FORBIDDEN is returned")
    void deleteEventForbidden() throws Exception {

        long eventId = 22L;

        doThrow(new ForbiddenException("You are not allowed to change" +
                " the event with id: " + eventId))
                .when(eventService).deleteEvent(any(Long.class), any(Jwt.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/delete?id=" + eventId))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value("FORBIDDEN"));
    }


    private EventCreateDto buildEventCreateDto(String vendorId) {
        return new EventCreateDto(TITLE, DESCRIPTION, PRICE, vendorId, AVAILABILITY, MAX_BOOKINGS);
    }

    private EventResponseDto buildEventResponseDto(String vendorId) {
        return new EventResponseDto(EVENT_ID, TITLE, DESCRIPTION, PRICE, vendorId, AVAILABILITY, MAX_BOOKINGS, CURRENT_BOOKINGS);
    }

    private EventUpdateDto buildEventUpdateDto(String vendorId) {
        return new EventUpdateDto(EVENT_ID, TITLE, DESCRIPTION, PRICE, vendorId, AVAILABILITY, MAX_BOOKINGS);

    }
}
