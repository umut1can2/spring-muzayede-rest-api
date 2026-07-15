package com.example.muzayede.service;

import com.example.muzayede.dto.LoginRequestDto;
import com.example.muzayede.dto.LoginResponseDto;
import com.example.muzayede.dto.UserCreateDto;
import com.example.muzayede.entity.User;
import com.example.muzayede.exception.ResourceNotFoundException;
import com.example.muzayede.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tools.jackson.databind.util.BeanUtil;

import java.math.BigDecimal;

@Service
public class UserService {

    /* Autowired yerine bu oneriliyor. */
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User CreateUser(UserCreateDto user)
    {
        User newUser = new User();

        BeanUtils.copyProperties(user, newUser);
        newUser.setBalance(BigDecimal.ZERO);
        newUser.setBlockedBalance(BigDecimal.ZERO);
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(newUser);
        return newUser;
    }

    public void addBalance(Long userId, BigDecimal amount)
    {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(userId.toString() +
                        " id'li Kullanici bulunamadi!"));

        user.setBalance(user.getBalance().add(amount));
        userRepository.save(user);
    }

    public LoginResponseDto login(LoginRequestDto dto)
    {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Username or PASSWORD are wrong!"));

        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword()))
        {
            throw new RuntimeException("Username or PASSWORD are wrong!");
        }

        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
        return new LoginResponseDto(token, user.getUsername());
    }
}
