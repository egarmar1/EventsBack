package com.kike.events.bookings.service.impl;

import com.google.zxing.common.BitMatrix;
import com.kike.events.bookings.constants.HistoryType;
import com.kike.events.bookings.constants.State;
import com.kike.events.bookings.dto.BookingCreateDto;
import com.kike.events.bookings.dto.BookingKafkaMessageDto;
import com.kike.events.bookings.dto.BookingResponseDto;
import com.kike.events.bookings.dto.client.EventResponseDto;
import com.kike.events.bookings.dto.client.EventsHistoryDto;
import com.kike.events.bookings.dto.client.UserTypeDto;
import com.kike.events.bookings.entity.Booking;
import com.kike.events.bookings.exception.*;
import com.kike.events.bookings.mapper.BookingCreateMapper;
import com.kike.events.bookings.mapper.BookingKafkaMessageMapper;
import com.kike.events.bookings.mapper.BookingResponseMapper;
import com.kike.events.bookings.repository.BookingRepository;
import com.kike.events.bookings.service.IBookingService;
import com.kike.events.bookings.service.auth.JwtService;
import com.kike.events.bookings.service.client.EventsFeignClient;
import com.kike.events.bookings.service.client.EventsHistoryFeignClient;
import com.kike.events.bookings.service.client.UserFeignClient;
import com.kike.events.bookings.utils.QRUtility;
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
import java.util.UUID;


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
    public void createBooking(BookingCreateDto bookingCreateDto, String userId, Jwt jwt) {

        String actualUserId = getTargetUserId(userId, jwt);
        checkUserIsClientOrAdmin(jwt);

        Optional<Booking> existingBooking = bookingRepository.findByUserIdAndEventId(actualUserId, bookingCreateDto.getEventId());

        if (existingBooking.isPresent())
            throw new BookingAlreadyExistsException("The book with eventId: " +
                    bookingCreateDto.getEventId() + " and userId: " +
                    actualUserId + " already exists");

        Booking booking = BookingCreateMapper.mapToBooking(bookingCreateDto, new Booking());
        booking.setUserId(actualUserId);

        UUID randomUUID = UUID.randomUUID();

        BitMatrix qrCodeMatrix = QRUtility.generateQRCode(randomUUID.toString());
        QRUtility.saveQRLocally(qrCodeMatrix,randomUUID);
        booking.setQrUUID(randomUUID.toString());

        eventsFeignClient.updateCurrentBookingsCount(bookingCreateDto.getEventId());
        Booking bookingSaved = bookingRepository.save(booking);

        EventsHistoryDto eventsHistoryDto = new EventsHistoryDto(booking.getUserId(),
                booking.getEventId(),
                HistoryType.BOOKED_EVENT);



        BookingKafkaMessageDto bookingKafkaMessageDto = BookingKafkaMessageMapper.mapToBookingDto(bookingSaved, new BookingKafkaMessageDto());
        bookingKafkaMessageDto.setJwt(jwt);
        log.info("The BookingKafkaMessageDto before sending to notifications is: " + bookingKafkaMessageDto);

        streamBridge.send("bookingQrCodeCreated", bookingKafkaMessageDto);


        streamBridge.send("create-event-history", eventsHistoryDto);

    }



    private void checkUserIsClientOrAdmin(Jwt jwt) {
        String type = userFeignClient.getType(jwt.getClaim("sub")).getBody().getType();
        boolean isAdmin = jwtService.getRealmRoles(jwt).contains("admin");


        if(!Objects.equals(type, "client") && !isAdmin)
            throw new UnauthorizedException("You are not a client, you are not able to create a booking");
    }

    @Override
    public BookingResponseDto fetchbooking(Long eventId, String userId) {

        Booking booking = bookingRepository.findByUserIdAndEventId(userId, eventId).orElseThrow(() ->
                new ResourceNotFoundException("Booking", "userId or eventId", userId + " and " + eventId + " respectively"));

        return BookingResponseMapper.mapToBookingDto(booking, new BookingResponseDto());
    }

    @Override
    public boolean updatebooking(BookingCreateDto bookingCreateDto, String userId, Jwt jwt) {

        String actualUserId = getTargetUserId(userId, jwt);

        Booking booking = bookingRepository.findByUserIdAndEventId(userId, bookingCreateDto.getEventId()).orElseThrow(() ->
                new ResourceNotFoundException("Booking", "userId or eventId", actualUserId + " and " + bookingCreateDto.getEventId() + " respectively"));


        BookingCreateMapper.mapToBooking(bookingCreateDto, booking);
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

        Booking booking = bookingRepository.findByUserIdAndEventId(actualUserId, eventId).orElseThrow(() ->
                new ResourceNotFoundException("Booking", "userId or eventId", actualUserId + " and " + eventId + " respectively"));

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

    @Override
    public byte[] fetchQr(Long bookingId, Jwt jwt) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new ResourceNotFoundException("Booking", "bookingId", bookingId.toString()));

        String sub = jwt.getClaim("sub");
        if(!Objects.equals(sub, booking.getUserId()) && !jwtService.getRealmRoles(jwt).contains("admin")){
            throw new UnauthorizedException("You are not allowed to see this resource");
        }

        return QRUtility.loadLocalQR(booking.getQrUUID());
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
