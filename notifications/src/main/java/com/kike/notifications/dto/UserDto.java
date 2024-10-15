package com.kike.notifications.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDto {


    @NotEmpty(message = "User id cannot be empty")
    private String userId;

    @NotEmpty(message = "Full name cannot be empty")
    private String fullName;

    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotEmpty(message = "Full name cannot be empty")
    private Long typeId;
}
