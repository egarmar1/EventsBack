package com.example.user.service;

import com.example.user.dto.ResponseDto;
import com.example.user.dto.UserDto;

public interface IUserService {

    boolean updateInfo(UserDto userDto);
}
