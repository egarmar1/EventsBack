package com.kike.suscription.service.client;

import com.kike.suscription.dto.clients.UserTypeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "users")
public interface UserFeignClient {

    @GetMapping(value = "/api/type")
    public ResponseEntity<UserTypeDto> getType(@RequestParam String userId);
}
