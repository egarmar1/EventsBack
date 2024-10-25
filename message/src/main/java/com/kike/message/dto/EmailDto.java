package com.kike.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;

import java.time.LocalDateTime;

import static org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {

    private String recipientEmail;
    private String subject;
    private String body;
    private String urlImage;
    private String senderEmail;
    private LocalDateTime timestamp;
    private Jwt jwt;
}
