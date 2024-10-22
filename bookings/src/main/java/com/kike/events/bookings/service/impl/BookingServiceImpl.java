package com.kike.events.bookings.service.impl;

import com.kike.events.bookings.constants.HistoryType;
import com.kike.events.bookings.constants.State;
import com.kike.events.bookings.dto.BookingDto;
import com.kike.events.bookings.dto.client.EventResponseDto;
import com.kike.events.bookings.dto.client.EventsHistoryDto;
import com.kike.events.bookings.dto.client.UserTypeDto;
import com.kike.events.bookings.entity.Booking;
import com.kike.events.bookings.exception.*;
import com.kike.events.bookings.mapper.BookingMapper;
import com.kike.events.bookings.repository.BookingRepository;
import com.kike.events.bookings.service.IBookingService;
import com.kike.events.bookings.service.auth.JwtService;
import com.kike.events.bookings.service.client.EventsFeignClient;
import com.kike.events.bookings.service.client.EventsHistoryFeignClient;
import com.kike.events.bookings.service.client.UserFeignClient;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@AllArgsConstructor
public class BookingServiceImpl implements IBookingService {

    private BookingRepository bookingRepository;
    private EventsFeignClient eventsFeignClient;
    private EventsHistoryFeignClient eventsHistoryFeignClient;
    private UserFeignClient userFeignClient;
    private JwtService jwtService;
    private StreamBridge streamBridge;

    private static final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);


    @Override
    public void createBooking(BookingDto bookingDto, String userId, Jwt jwt) {

        String actualUserId = getTargetUserId(userId, jwt);
        checkUserIsClientOrAdmin(jwt);

        Optional<Booking> existingBooking = bookingRepository.findByUserIdAndEventId(actualUserId, bookingDto.getEventId());

        if (existingBooking.isPresent())
            throw new BookingAlreadyExistsException("The book with serviceId: " +
                    bookingDto.getEventId() + " and userId: " +
                    actualUserId + " already exists");

        Booking booking = BookingMapper.mapToBooking(bookingDto, new Booking());
        booking.setUserId(actualUserId);

        eventsFeignClient.updateCurrentBookingsCount(bookingDto.getEventId());
        bookingRepository.save(booking);

        EventsHistoryDto eventsHistoryDto = new EventsHistoryDto(booking.getUserId(),
                booking.getEventId(),
                HistoryType.BOOKED_EVENT);

        streamBridge.send("create-event-history", eventsHistoryDto);

    }

    private void checkUserIsClientOrAdmin(Jwt jwt) {
        String type = userFeignClient.getType(jwt.getClaim("sub")).getBody().getType();
        boolean isAdmin = jwtService.getRealmRoles(jwt).contains("admin");


        if(!Objects.equals(type, "client") && !isAdmin)
            throw new UnauthorizedException("You are not a client, you are not able to create a booking");
    }

    @Override
    public BookingDto fetchbooking(Long eventId, String userId) {

        Booking booking = bookingRepository.findByUserIdAndEventId(userId, eventId).orElseThrow(() ->
                new ResourceNotFoundException("Booking", "userId or eventId", userId + " and " + eventId + " respectively"));

        return BookingMapper.mapToBookingDto(booking, new BookingDto());
    }

    @Override
    public boolean updatebooking(BookingDto bookingDto, String userId, Jwt jwt) {

        String actualUserId = getTargetUserId(userId, jwt);

        Booking booking = bookingRepository.findByUserIdAndEventId(userId, bookingDto.getEventId()).orElseThrow(() ->
                new ResourceNotFoundException("Booking", "userId or eventId", actualUserId + " " + bookingDto.getEventId() + " respectively"));


        BookingMapper.mapToBooking(bookingDto, booking);
        booking.setUserId(userId);

        bookingRepository.save(booking);

        return true;
    }

    @NotNull
    private String getTargetUserId(String userId, Jwt jwt) {
        if (jwt == null)
            throw new UnauthorizedException("Oauth2 access token is required");

        String actualUserId = jwtService.getRealmRoles(jwt).contains("admin") ?
                userId : jwtService.getUserId(jwt);

        if (actualUserId.isEmpty())
            throw new MissingUserIdFromAdminException();

        return actualUserId;
    }

    @Override
    public void deleteBooking(Long eventId, String userId, Jwt jwt) {


        String actualUserId = getTargetUserId(userId, jwt);

        Booking booking = bookingRepository.findByUserIdAndEventId(userId, eventId).orElseThrow(() ->
                new ResourceNotFoundException("Booking", "userId or eventId", actualUserId + " " + eventId + " respectively"));

        bookingRepository.deleteById(booking.getId());
    }

    @Override
    @Transactional
    public void deleteAllbookingsWithEventId(Long eventId) {
        List<Booking> bookings = bookingRepository.findByEventId(eventId);

        bookings.forEach(booking -> bookingRepository.deleteById(booking.getId()));
    }

    @Override
    public void setBookingAsAttended(Long eventId, String userId, Jwt jwt) {

        String sub = jwt.getClaim("sub");

        checkTypeVendor(sub);
        checkEventOwner(eventId, sub);


        Booking booking = bookingRepository.findByUserIdAndEventId(userId, eventId).orElseThrow(
                () -> new ResourceNotFoundException("Booking",
                        "userId or eventId", userId + " and " + eventId + " respectively"));

        booking.setState(State.ATTENDED);

        bookingRepository.save(booking);


        EventsHistoryDto eventsHistoryDto = new EventsHistoryDto(userId, eventId, HistoryType.ATTENDED_EVENT);

        log.info("eventsHistoryDto: " + eventsHistoryDto);

        eventsHistoryFeignClient.updateEventHistory(eventsHistoryDto);


    }

    private void checkEventOwner(Long eventId, String sub) {
        ResponseEntity<EventResponseDto> eventResponse = eventsFeignClient.fetchEvent(eventId);

        String vendorIdOfEvent = eventResponse.getBody().getVendorId();

        if(!vendorIdOfEvent.equals(sub))
            throw new UnauthorizedException("You are not the owner of the event with eventId: " + eventId);
    }

    private void checkTypeVendor(String vendorId) {
        ResponseEntity<UserTypeDto> typeResponse = userFeignClient.getType(vendorId);

        String type = typeResponse.getBody().getType();

        if (!type.equals("vendor"))
            throw new IncorrectTypeOfUserException(vendorId, "vendor", type);
    }

//    @Override
//    public BookingDto fetchBooking(Long userId, Long serviceId) {
//        return null;
//    }
//
//    @Override
//    public boolean updateBooking(BookingDto bookingDto) {
//        return false;
//    }
//
//    @Override
//    public void deleteBooking(Long id) {
//
//    }
}
