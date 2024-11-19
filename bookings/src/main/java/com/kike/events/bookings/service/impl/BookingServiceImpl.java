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
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;


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

        checkBookingDoesNotExists(bookingCreateDto, actualUserId);

        Booking booking = BookingCreateMapper.mapToBooking(bookingCreateDto, new Booking());
        booking.setUserId(actualUserId);


        generateAndSaveQrCode(booking);


        updateEventBookingCount(bookingCreateDto);
        Booking bookingSaved = bookingRepository.save(booking);

        sendKafkaEvents(jwt, booking, bookingSaved);

    }

    private void sendKafkaEvents(Jwt jwt, Booking booking, Booking bookingSaved) {
        EventsHistoryDto eventsHistoryDto = new EventsHistoryDto(booking.getUserId(),
                booking.getEventId(),
                HistoryType.BOOKED_EVENT);


        BookingKafkaMessageDto bookingKafkaMessageDto = BookingKafkaMessageMapper.mapToBookingDto(bookingSaved, new BookingKafkaMessageDto());
        bookingKafkaMessageDto.setJwt(jwt.getTokenValue());
        log.info("The BookingKafkaMessageDto before sending to notifications is: " + bookingKafkaMessageDto);

        streamBridge.send("bookingQrCodeCreated", bookingKafkaMessageDto);

        streamBridge.send("create-event-history", eventsHistoryDto);
    }

    private void updateEventBookingCount(BookingCreateDto bookingCreateDto) {
        try {
            eventsFeignClient.updateCurrentBookingsCount(bookingCreateDto.getEventId());
        } catch (FeignException feignException) {
            if (feignException.status() == HttpStatus.CONFLICT.value()) {
                log.info("Exception FeignException thrown");
                throw new CurrentBookingsGreaterThanMaxException(feignException.getMessage());
            }
        }
    }

    private static void generateAndSaveQrCode(Booking booking) {
        UUID randomUUID = UUID.randomUUID();

        BitMatrix qrCodeMatrix = QRUtility.generateQRCode(randomUUID.toString());
        QRUtility.saveQRLocally(qrCodeMatrix, randomUUID);
        booking.setQrUUID(randomUUID.toString());
        booking.setQrIsDeleted(false);
    }

    private void checkBookingDoesNotExists(BookingCreateDto bookingCreateDto, String actualUserId) {
        Optional<Booking> existingBooking = bookingRepository.findByUserIdAndEventId(actualUserId, bookingCreateDto.getEventId());

        if (existingBooking.isPresent())
            throw new BookingAlreadyExistsException("The book with eventId: " +
                    bookingCreateDto.getEventId() + " and userId: " +
                    actualUserId + " already exists");
    }


    private void checkUserIsClientOrAdmin(Jwt jwt) {
        String type = userFeignClient.getType(jwt.getClaim("sub")).getBody().getType();
        boolean isAdmin = jwtService.getRealmRoles(jwt).contains("admin");


        if (!Objects.equals(type, "client") && !isAdmin)
            throw new UnauthorizedException("You are not a client, you are not able to create a booking");
    }

    @Override
    public BookingResponseDto fetchbooking(Long eventId, String userId, Jwt jwt) {

        String actualUser = getTargetUserId(userId, jwt);

        Booking booking = bookingRepository.findByUserIdAndEventId(actualUser, eventId).orElseThrow(() ->
                new ResourceNotFoundException("Booking", "userId or eventId", actualUser + " and " + eventId + " respectively"));

        if (!jwtService.getRealmRoles(jwt).contains("admin") && !Objects.equals(booking.getUserId(), actualUser)) {
            throw new ForbiddenException("You don't have permissions for info about user " + booking.getUserId());
        }

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
    public void setBookingAsAttended(String qrUUID, Jwt jwt) {


        Booking booking = bookingRepository.findByQrUUID(qrUUID).orElseThrow(
                () -> new ResourceNotFoundException("Booking",
                        "qr UUID", qrUUID));

        if (booking.getState() != State.ACCEPTED)
            throw new InvalidBookingStateException("The state is: " + booking.getState() + " but it should be: " + State.ACCEPTED);


        String sub = jwt.getClaim("sub");

        checkTypeVendor(sub);
        checkEventOwner(booking.getEventId(), sub);

        booking.setState(State.ATTENDED);
        booking.setQrExpirationDate(LocalDateTime.now().plusDays(1));

        bookingRepository.save(booking);


        EventsHistoryDto eventsHistoryDto = new EventsHistoryDto(booking.getUserId(), booking.getEventId(), HistoryType.ATTENDED_EVENT);

        log.info("eventsHistoryDto: " + eventsHistoryDto);

        eventsHistoryFeignClient.updateEventHistory(eventsHistoryDto);
    }

    @Override
    public byte[] fetchQr(Long bookingId, Jwt jwt) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new ResourceNotFoundException("Booking", "bookingId", bookingId.toString()));

        String sub = jwt.getClaim("sub");
        if (!Objects.equals(sub, booking.getUserId()) && !jwtService.getRealmRoles(jwt).contains("admin")) {
            throw new UnauthorizedException("You are not allowed to see this resource");
        }

        return QRUtility.loadLocalQR(booking.getQrUUID());
    }

    @Override
    public List<BookingResponseDto> fetchBookingsOfUserByState(String userId, State state, Jwt jwt) {
        log.info("Starting fetchBookingsOfUserByState method in service ...");
        String actualUserId = getTargetUserId(userId, jwt);

        List<Booking> bookings = bookingRepository.findByUserIdAndState(actualUserId, state);

        if (bookings.isEmpty())
            return Collections.emptyList();

        if (!jwtService.getRealmRoles(jwt).contains("admin"))
            if (!bookings.getFirst().getUserId().equals(actualUserId))
                throw new ForbiddenException("You don't have enough privileges to see this info");


        List<BookingResponseDto> bookingsDto = bookings.stream().map(
                b -> BookingResponseMapper.mapToBookingDto(b, new BookingResponseDto())
        ).toList();

        log.info("Finishing fetchBookingsOfUserByState method in service ...");
        return bookingsDto;
    }

    @Override
    @Transactional
    public void deleteExpiredQRCodes() {
        log.info("Starting deleteExpiredQRCodes method in service ...");
        List<Booking> expiredQrCodes = bookingRepository.findAll().stream()
                .filter(b -> b.getQrExpirationDate() != null
                        && b.getQrExpirationDate().isBefore(LocalDateTime.now())
                        && !b.getQrIsDeleted()
                ).toList();

        removeExpiredQrCodes(expiredQrCodes);
        log.info("Finishing deleteExpiredQRCodes method in service ...");
    }

    private void removeExpiredQrCodes(List<Booking> expiredQrCodes) {

        String directoryPath = "C:/Users/kikeg/Documents/MSPROJECT/EventsBack/qrCodes/";

        expiredQrCodes.forEach(booking -> {
            String filePath = directoryPath + booking.getQrUUID() + ".png";
            File file = new File(filePath);

            if (file.exists()) {
                log.info("Setting file as deleted in database...");
                booking.setQrIsDeleted(true);
                bookingRepository.save(booking);

                log.info("Deleting file: " + filePath);
                file.delete();
            } else {
                log.info("File: " + filePath + " not deleted because it was not found");
            }
        });

    }

    private void checkEventOwner(Long eventId, String sub) {
        ResponseEntity<EventResponseDto> eventResponse = eventsFeignClient.fetchEvent(eventId);

        String vendorIdOfEvent = eventResponse.getBody().getVendorId();

        if (!vendorIdOfEvent.equals(sub))
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
