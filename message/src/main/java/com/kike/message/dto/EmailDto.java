package com.kike.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EmailDto {

    private String recipientEmail;
    private String subject;
    private String body;
    private String senderEmail;
    private LocalDateTime timestamp;
}
