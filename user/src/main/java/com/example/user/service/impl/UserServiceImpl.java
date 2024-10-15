package com.example.user.service.impl;

import com.example.user.dto.UserDto;
import com.example.user.dto.UserTypeDto;
import com.example.user.entity.Users;
import com.example.user.entity.UserType;
import com.example.user.exception.ResourceNotFoundException;
import com.example.user.mapper.UserMapper;
import com.example.user.mapper.UserTypeMapper;
import com.example.user.repository.UserRepository;
import com.example.user.repository.UserTypeRepository;
import com.example.user.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    private UserRepository userRepository;
    private UserTypeRepository userTypeRepository;
    @Override
    public boolean updateUserInfo(UserDto userDto) {


        Users user = userRepository.findById(userDto.getUserId()).orElseThrow(() ->
                new ResourceNotFoundException("User", "userId", userDto.getUserId()));


        UserMapper.mapToUser(userDto, user);

        userRepository.save(user);

        return true;
    }

    @Override
    public UserDto fetchUserInfo(String userId) {

        Users user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", userId)
        );

        return UserMapper.mapToUserDto(user, new UserDto());
    }

    @Override
    public UserTypeDto getUserTypeByUserId(String userId) {

        UserDto userDto = fetchUserInfo(userId);
        Long userTypeId = userDto.getTypeId();

        UserType userType = userTypeRepository.findById(userTypeId).orElseThrow(
                () -> new ResourceNotFoundException("UserType", "typeId", userTypeId.toString())
        );

        return UserTypeMapper.mapToUserTypeDto(userType, new UserTypeDto());
    }


}
