package com.kike.events.bookings.service.client;

import com.kike.events.bookings.dto.client.UserTypeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "users")
public interface UserFeignClient {

    @GetMapping(value = "/api/type")
    public ResponseEntity<UserTypeDto> getType(@RequestParam String userId);
}
