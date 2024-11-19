package com.kike.events.bookings.config;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

@Configuration
public class FeignClientConfiguration {

    @Bean
    public RequestInterceptor jwtRequestInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                var authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null && authentication.getCredentials() instanceof Jwt) {
                    Jwt jwt = (Jwt) authentication.getCredentials();
                    requestTemplate.header("Authorization", "Bearer " + jwt.getTokenValue());
                }
            }
        };
    }

}
