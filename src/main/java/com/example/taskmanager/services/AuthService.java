package com.example.taskmanager.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.taskmanager.dto.LoginRequest;
import com.example.taskmanager.dto.RigisterDTO;
import com.example.taskmanager.enums.Role;
import com.example.taskmanager.mapper.UserMapper;
import com.example.taskmanager.model.AdminProfileEntity;
import com.example.taskmanager.model.UserEntity;
import com.example.taskmanager.model.UserProfileEntity;
import com.example.taskmanager.repository.AdminProfileRepository;
import com.example.taskmanager.repository.UserProfileRepository;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.security.jwt.JwtService;
import com.example.taskmanager.security.user.CustomUserDetailsService;

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
   @Autowired
   private AuthenticationManager authenticationManager;
   @Autowired
   private CustomUserDetailsService userDetailsService;
   @Autowired
   private JwtService jwtService;

   public String register(RigisterDTO rigisterDTO) {
      if (userRepository.existsByUsername(rigisterDTO.getUsername())) {
         throw new RuntimeException("Username already exists");
      }
      UserEntity userEntity = userRepository.save(userMapper.toEntity(rigisterDTO));
      if (rigisterDTO.getRole() == Role.USER) {
         userProfileRepository.save(UserProfileEntity.builder().address(rigisterDTO.getAddress())
               .fullName(rigisterDTO.getFullname()).build());
      } else {
         adminProfileRepository
               .save(AdminProfileEntity.builder().department(rigisterDTO.getDepartment()).user(userEntity).build());
      }
      return "Success";
   }

   public String login(LoginRequest request) {
      authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                  request.getUsername(),
                  request.getPassword()));
      UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
      return jwtService.generateToken(user);
   }
}
