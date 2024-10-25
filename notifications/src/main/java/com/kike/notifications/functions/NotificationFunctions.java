package com.kike.notifications.functions;

import com.kike.notifications.dto.BookingKafkaMessageDto;
import com.kike.notifications.dto.EmailDto;
import com.kike.notifications.dto.EventResponseDto;
import com.kike.notifications.dto.UserDto;
import com.kike.notifications.service.client.EventsFeignClient;
import com.kike.notifications.service.client.SuscriptionFeignClient;
import com.kike.notifications.service.client.UserFeignClient;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

@Configuration
@AllArgsConstructor
public class NotificationFunctions {

    private static final Logger log = LoggerFactory.getLogger(NotificationFunctions.class);
    private SuscriptionFeignClient suscriptionFeignClient;
    private UserFeignClient userFeignClient;
    private EventsFeignClient eventsFeignClient;
    private StreamBridge streamBridge;

    @Bean
    public Consumer<EventResponseDto> eventCreated() {
        return (eventInfo) -> {
            log.info("Executing eventCreated Consumer");

            UserDto vendorDto = userFeignClient.fetchUserInfo(eventInfo.getVendorId()).getBody();

            List<String> userIds = suscriptionFeignClient.fetchSuscriptionsByVendorId(eventInfo.getVendorId()).getBody();

            //// check fetchSuscription to see if it can return empty list, and is empty list a problem for forEach()?
            userIds.forEach(userId -> {
                UserDto clientDto = userFeignClient.fetchUserInfo(userId).getBody();

                EmailDto email = new EmailDto();
                email.setRecipientEmail(clientDto.getEmail());
                email.setSubject("New event from " + vendorDto.getFullName());
                email.setBody(eventInfo.getTitle() + "\n" + eventInfo.getDescription());
                email.setSenderEmail("kikegm2001@gmail.com");
                email.setTimestamp(LocalDateTime.now());

                streamBridge.send("email-notification", email);
            });


        };
    }

    @Bean
    public Consumer<BookingKafkaMessageDto> bookingQrCodeCreated() {
        log.info("Executing bookingQrCodeCreated Consumer");
        return bookingKafkaMessageDto -> {
            log.info("BookingResponseDto: " + bookingKafkaMessageDto);

            EventResponseDto eventDto = eventsFeignClient.fetchEvent(bookingKafkaMessageDto.getEventId()).getBody();

            UserDto userDto = userFeignClient.fetchUserInfo(bookingKafkaMessageDto.getUserId()).getBody();

            EmailDto email = new EmailDto(userDto.getEmail(),
                    "Qr for event" + eventDto.getTitle(),
                    "Hi " + userDto.getFullName() + " here is your QR for the event " + eventDto.getTitle(),
                    "http://localhost:8072/fastbook/booking/api/fetchQR/bookingId/" + bookingKafkaMessageDto.getId(),
                    "kikegm2001@gmail.com", LocalDateTime.now(),
                    bookingKafkaMessageDto.getJwt());


            streamBridge.send("email-notification", email);
//            String qrCodeInBase64 = bookingResponseDto.getQrUUID();
//            byte[] qrImage = Base64.getDecoder().decode(qrCodeInBase64);

        };
    }
}
