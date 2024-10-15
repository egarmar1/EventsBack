package com.kike.notifications.service.client;

import com.kike.notifications.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "users")
public interface UserFeignClient {

    @GetMapping("/api/fetchUserInfo")
    public ResponseEntity<UserDto> fetchUserInfo(@RequestParam String userId);
}
