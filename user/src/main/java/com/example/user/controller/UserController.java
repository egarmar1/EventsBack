package com.example.user.controller;

import com.example.user.dto.KeyCloakEventDto;
import com.example.user.dto.ResponseDto;
import com.example.user.dto.UserDto;
import com.example.user.dto.UserTypeDto;
import com.example.user.entity.Users;
import com.example.user.repository.UserRepository;
import com.example.user.service.IUserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.user.constants.UserConstants.*;
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

    @GetMapping("/fetchUserInfo")
    public ResponseEntity<UserDto> fetchUserInfo(@RequestParam String userId) {

        UserDto userDto = userService.fetchUserInfo(userId);


        return ResponseEntity
                .status(OK)
                .body(userDto);
    }

    @GetMapping("/fetchAllUsersInfo")
    public ResponseEntity<List<UserDto>> fetchAllUsersInfo() {

        List<UserDto> userDto = userService.fetchAllUsersInfo();


        return ResponseEntity
                .status(OK)
                .body(userDto);
    }

    @PutMapping("/updateInfo")
    public ResponseEntity<ResponseDto> updateUserInfo(@Valid @RequestBody UserDto userDto) {

        userService.updateUserInfo(userDto);

        return ResponseEntity
                .status(OK)
                .body(new ResponseDto(STATUS_200, MESSAGE_200));
    }


    @GetMapping("/type")
    public ResponseEntity<UserTypeDto> getType(@RequestParam String userId){


        UserTypeDto userType = userService.getUserTypeByUserId(userId);

        return ResponseEntity.status(OK).body(userType);
    }

    @PutMapping("/updateUsersTableInfo")
    public ResponseEntity<ResponseDto> updateUsersTableInfo(@RequestParam KeyCloakEventDto keyCloakEventDto){

        return ResponseEntity.status(OK).body(new ResponseDto(STATUS_200,MESSAGE_200));
    }

}
