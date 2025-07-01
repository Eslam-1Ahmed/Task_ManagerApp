package com.example.taskmanager.dto;


import com.example.taskmanager.enums.Role;
import com.example.taskmanager.validation.ValidPassword;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RigisterDTO {

    @NotNull(message = "The username can't be null")
    @NotBlank(message = "The username can't be empty")
    private String username;
    
    @Email(message = "Email must be correct")
    private String email;

    @ValidPassword(minLength = 10)
    private String password;

    @NotNull(message = "The Role can't be null")
    private Role role;

    @NotNull(message = "The fullname can't be null")
    @NotBlank(message = "The fullname can't be empty")
    private String fullname;

    @NotNull(message = "The address can't be null")
    @NotBlank(message = "The address can't be empty")
    private String address;
    
    @Nullable
    private String department;
}
