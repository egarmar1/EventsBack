package com.kike.notifications.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDto {

    private String recipientEmail;
    private String subject;
    private String body;
    private String senderEmail;
    private LocalDateTime timestamp;
}
