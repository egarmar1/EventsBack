package com.kike.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayserverApplication.class, args);
    }


    @Bean
    public RouteLocator fastBookRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
				.route( p -> p
						.path("/fastbook/event/**")
						.filters(f -> f.rewritePath("/fastbook/event/(?<segment>.*)", "/${segment}" )
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
						.uri("lb://EVENT"))

                .route( p -> p
                        .path("/fastbook/booking/**")
                        .filters(f -> f.rewritePath("/fastbook/booking/(?<segment>.*)", "/${segment}" ))
                        .uri("lb://BOOKING")).build();

    }
}
