package com.example.taskmanager.mapper;

import org.springframework.stereotype.Component;

import com.example.taskmanager.dto.RigisterDTO;
import com.example.taskmanager.model.UserEntity;

@Component
public class UserMapper {
    public UserEntity toEntity(RigisterDTO rigisterDTO) {
        return UserEntity.builder().email(rigisterDTO.getEmail()).password(rigisterDTO.getPassword())
                .role(rigisterDTO.getRole()).username(rigisterDTO.getUsername()).build();
    }
}
