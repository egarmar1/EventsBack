package com.example.user.service;

import com.example.user.dto.UserDto;
import com.example.user.dto.UserTypeDto;
import com.example.user.entity.UserType;

public interface IUserService {

    boolean updateUserInfo(UserDto userDto);

    UserDto fetchUserInfo(String userId);

    UserTypeDto getUserTypeByUserId(String userId);
}
