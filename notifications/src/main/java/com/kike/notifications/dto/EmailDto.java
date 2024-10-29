package com.kike.notifications.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


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
    private String jwt;
}
