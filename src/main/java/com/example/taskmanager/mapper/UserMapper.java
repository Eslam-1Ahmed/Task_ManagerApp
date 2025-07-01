package com.example.taskmanager.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.taskmanager.dto.RigisterDTO;
import com.example.taskmanager.model.UserEntity;

@Component
public class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntity toEntity(RigisterDTO rigisterDTO) {
        return UserEntity.builder().email(rigisterDTO.getEmail())
                .password(passwordEncoder.encode(rigisterDTO.getPassword()))
                .role(rigisterDTO.getRole()).username(rigisterDTO.getUsername()).build();
    }
}
