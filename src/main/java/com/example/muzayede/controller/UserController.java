package com.example.muzayede.controller;

import com.example.muzayede.dto.UserCreateDto;
import com.example.muzayede.entity.User;
import com.example.muzayede.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @PostMapping("/add-balance/{id}")
    public void addBalance(@PathVariable Long id,
                           @RequestBody @DecimalMin(value = "1.0",
                           message = "You can add min 1.0 to your account!") BigDecimal amount)
    {
        userService.addBalance(id, amount);
    }

}
