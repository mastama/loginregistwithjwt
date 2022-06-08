package com.example.chrombit.service.impl;

import com.example.chrombit.dto.RegisterDto;
import com.example.chrombit.model.User;
import com.example.chrombit.payload.response.UserResponse;
import com.example.chrombit.repository.UserRepository;
import com.example.chrombit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public UserResponse create(RegisterDto registerDto) throws Exception {
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(registerDto.getPassword()));

        User userSaved = this.userRepository.save(user);
            UserResponse userResponse = new UserResponse();
            userResponse.setId(userSaved.getId());
            userResponse.setUsername(userSaved.getUsername());

        return userResponse;
    }
}
