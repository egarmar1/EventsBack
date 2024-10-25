package com.example.user.dto;

import lombok.Data;

@Data
public class KeyCloakEventDto {
    private String type;
    private String userId;
    private String clientId;
    private String ipAddress;

}
