package com.example.muzayede.controller;

import com.example.muzayede.dto.LoginRequestDto;
import com.example.muzayede.dto.LoginResponseDto;
import com.example.muzayede.dto.UserCreateDto;
import com.example.muzayede.entity.User;
import com.example.muzayede.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService)
    {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@Valid @RequestBody UserCreateDto dto)
    {
        return userService.CreateUser(dto);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginRequestDto dto)
    {
        return userService.login(dto);
    }
}
