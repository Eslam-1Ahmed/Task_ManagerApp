package com.example.taskmanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.taskmanager.dto.RigisterDTO;
import com.example.taskmanager.enums.Role;
import com.example.taskmanager.mapper.UserMapper;
import com.example.taskmanager.model.AdminProfileEntity;
import com.example.taskmanager.model.UserEntity;
import com.example.taskmanager.model.UserProfileEntity;
import com.example.taskmanager.repository.AdminProfileRepository;
import com.example.taskmanager.repository.UserProfileRepository;
import com.example.taskmanager.repository.UserRepository;

@Service
public class AuthService {

   @Autowired
   private UserRepository userRepository;
   @Autowired
   private UserProfileRepository userProfileRepository;

   @Autowired
   private AdminProfileRepository adminProfileRepository;

   @Autowired
   private UserMapper userMapper;

   public String register(RigisterDTO rigisterDTO) {
      UserEntity userEntity =  userRepository.save(userMapper.toEntity(rigisterDTO));
      if (rigisterDTO.getRole() == Role.USER) {
         userProfileRepository.save(UserProfileEntity.builder().address(rigisterDTO.getAddress())
               .fullName(rigisterDTO.getFullname()).build());
      } else {
         adminProfileRepository.save(AdminProfileEntity.builder().department(rigisterDTO.getDepartment()).user(userEntity).build());
      }
      return "Success";
   }
}
