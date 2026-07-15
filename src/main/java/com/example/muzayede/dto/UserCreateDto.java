package com.example.muzayede.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {
    @NotBlank(message = "Username cannot be empty!")
    @Size(min=3, max = 20, message = "Username's size must be between 3 and 20!")
    private String username;

    @NotBlank(message = "Username cannot be empty!")
    @Email(message = "Please use valid e-mail!")
    private String email;

    @NotBlank(message = "Password cannot be empty!")
    @Size(min=6, message = "Password must be at least 3 character.")
    private String password;
}
