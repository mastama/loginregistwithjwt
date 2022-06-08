package com.example.chrombit.controller;

import com.alibaba.fastjson.JSON;
import com.example.chrombit.dto.JwtResponseDto;
import com.example.chrombit.dto.LoginDto;
import com.example.chrombit.dto.RegisterDto;
import com.example.chrombit.model.User;
import com.example.chrombit.payload.response.UserResponse;
import com.example.chrombit.repository.UserRepository;
import com.example.chrombit.service.JpaUserDetailsService;
import com.example.chrombit.service.UserService;
import com.example.chrombit.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JpaUserDetailsService jpaUserDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    // create login
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody LoginDto loginDto) throws Exception {
        // authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        UserDetails userDetails = jpaUserDetailsService.loadUserByUsername(loginDto.getUsername());
        String jwtToken = jwtUtil.generateToken(userDetails);
        JwtResponseDto jwtResponse = new JwtResponseDto(jwtToken, 200, "login success");

        return new ResponseEntity<JwtResponseDto>(jwtResponse, HttpStatus.ACCEPTED);
    }

    // create register
    @PostMapping("/register")
    public ResponseEntity<String>create(@RequestBody RegisterDto registerDto) throws Exception {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        Optional<User> userOptional = userRepository.findByUsername(registerDto.getUsername());
        if (userOptional.isPresent()) {
            result.put("message", "Username Sudah Terdaftar");
            result.put("status", 400);
            result.put("error", "Username Invalid");
            String json = JSON.toJSON(result).toString();
            return new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
        } else {
            try {
                UserResponse user = userService.create(registerDto);
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(registerDto.getUsername(), registerDto.getPassword()));

                UserDetails userDetails = jpaUserDetailsService.loadUserByUsername(registerDto.getUsername());
                String jwtToken = jwtUtil.generateToken(userDetails);

                result.put("token", jwtToken);
                result.put("message", "Register Berhasil");
                result.put("status", 200);
                result.put("data", user);
                String json = JSON.toJSON(result).toString();
                return new ResponseEntity<>(json, HttpStatus.OK);

            } catch (Exception e) {
                System.out.println(e.getMessage());
                result.put("message", "Register Gagal");
                result.put("status", 500);
                result.put("error", e.getMessage());
                String json = JSON.toJSON(result).toString();
                return new ResponseEntity<>(json, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

}
