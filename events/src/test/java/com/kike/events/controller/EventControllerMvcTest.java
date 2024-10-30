package com.kike.events.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kike.events.constants.Availability;
import com.kike.events.dto.EventCreateDto;
import com.kike.events.service.impl.EventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

@WebMvcTest(EventController.class)
public class EventControllerMvcTest {

    private MockMvc mockMvc;

    @MockBean
    private EventServiceImpl eventService;

    private static final String TITLE = "Bachata Class in center of Valencia";
    private static final String DESCRIPTION = "Bachata class in Valencia, on October 25th, no min level";
    private static final BigDecimal PRICE = new BigDecimal("10.9");
    private static final Availability AVAILABILITY = Availability.AVAILABLE;
    private static final long MAX_BOOKINGS = 50L;
    private static final long CURRENT_BOOKINGS = 10L;
    private static final long EVENT_ID = 1L;


    @BeforeEach
    void setUp(WebApplicationContext context) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("GIVEN a vendor with the information to create an event WHEN the create event endpoint is called THEN the event is created in the database with the data indicated")
    void postMappingTest() throws Exception {
        String vendorId = "c21a4735-e357-4c18-99e9-b54da83dd89b";
        buildEventCreateDto(vendorId);


    }

    private EventCreateDto buildEventCreateDto(String vendorId) {
        return new EventCreateDto(TITLE, DESCRIPTION, PRICE, vendorId, AVAILABILITY, MAX_BOOKINGS);
    }
}
