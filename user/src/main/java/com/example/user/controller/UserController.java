package com.example.user.controller;

import com.example.user.constants.UserConstants;
import com.example.user.dto.ResponseDto;
import com.example.user.dto.UserDto;
import com.example.user.service.IUserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.user.constants.UserConstants.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Tag(
        name = "REST API for user info in FASTBOOK",
        description = "REST APIs for users in FASTBOOK to UPDATE AND" +
                " FETCH user information "
)
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private IUserService userService;

    @PutMapping("/updateInfo")
    public ResponseEntity<ResponseDto> updateUserInfo(@Valid @RequestBody UserDto userDto) {

        userService.updateInfo(userDto);

        return ResponseEntity
                .status(OK)
                .body(new ResponseDto(STATUS_200, MESSAGE_200));
    }
}
