package com.example.chrombit.service;

import com.example.chrombit.dto.RegisterDto;
import com.example.chrombit.payload.response.UserResponse;


public interface UserService {
   UserResponse create(RegisterDto registerDto) throws Exception;
}
