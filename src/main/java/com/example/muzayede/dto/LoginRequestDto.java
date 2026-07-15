package com.example.muzayede.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
    @NotBlank(message = "Kullanici adi bos birakilamaz!")
    private String username;

    @NotBlank(message = "Sifre bos birakilamaz!")
    private String password;
}
