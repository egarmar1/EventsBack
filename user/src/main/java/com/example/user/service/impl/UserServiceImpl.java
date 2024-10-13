package com.example.user.service.impl;

import com.example.user.dto.ResponseDto;
import com.example.user.dto.UserDto;
import com.example.user.entity.User;
import com.example.user.exception.ResourceNotFoundException;
import com.example.user.mapper.UserMapper;
import com.example.user.repository.UserRepository;
import com.example.user.service.IUserService;
import lombok.AllArgsConstructor;
import org.apache.catalina.mbeans.UserMBean;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    private UserRepository userRepository;
    @Override
    public boolean updateInfo(UserDto userDto) {


        User user = userRepository.findById(userDto.getUserId()).orElseThrow(() ->
                new ResourceNotFoundException("User", "userId", userDto.getUserId()));


        UserMapper.mapToUser(userDto, user);

        userRepository.save(user);

        return true;
    }
}
