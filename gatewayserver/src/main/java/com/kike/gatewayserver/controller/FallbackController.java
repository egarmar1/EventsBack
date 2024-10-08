package com.kike.gatewayserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {
    // In a real scenario, a message will be sent to the support team.
    @RequestMapping("/contactSupport")
    public Mono<String> contactSupport() {
        return Mono.just("An error ocurred. Please try after some time" +
                " or reach contact support Team");
    }


}
