package com.kike.notifications.functions;

import com.kike.notifications.dto.EmailDto;
import com.kike.notifications.dto.EventResponseDto;
import com.kike.notifications.dto.UserDto;
import com.kike.notifications.service.client.SuscriptionFeignClient;
import com.kike.notifications.service.client.UserFeignClient;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Configuration
@AllArgsConstructor
public class NotificationFunctions {

    private static final Logger log = LoggerFactory.getLogger(NotificationFunctions.class);
    private SuscriptionFeignClient suscriptionFeignClient;
    private UserFeignClient userFeignClient;
    private StreamBridge streamBridge;

    @Bean
    public Consumer<EventResponseDto> eventCreated(){
        return (eventInfo) -> {
            log.info("Executing eventCreated Consumer");

            UserDto vendorDto = userFeignClient.fetchUserInfo(eventInfo.getVendorId()).getBody();

            List<String> userIds = suscriptionFeignClient.fetchSuscriptionsByVendorId(eventInfo.getVendorId()).getBody();


            userIds.forEach(userId -> {
                UserDto clientDto = userFeignClient.fetchUserInfo(userId).getBody();

                EmailDto email = new EmailDto("kikegm2001@gmail.com",
                        "New event from " + vendorDto.getFullName(),
                        eventInfo.getTitle() + "\n" + eventInfo.getDescription(),
                        clientDto.getEmail(), LocalDateTime.now());

                streamBridge.send("email-notification",email);
            });


        };
    }
}
