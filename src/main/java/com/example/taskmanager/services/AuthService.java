package com.example.taskmanager.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.example.taskmanager.dto.LoginRequest;
import com.example.taskmanager.dto.RigisterDTO;
import com.example.taskmanager.enums.Role;
import com.example.taskmanager.exception.ForbiddenException;
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

   public Map<String, String> refreshAccessToken(String refreshToken) {
      String username = jwtService.extractUsername(refreshToken);
      UserEntity user = userDetailsService.loadUserByUsername(username);
      if (!jwtService.isTokenValid(refreshToken, user.getUsername())) {
         throw new ForbiddenException("Invalid or expired refresh token");
      }
      String newAccessToken = jwtService.generateToken(user);
      String newRefreshToken = jwtService.generateRefershToken(user);

      Map<String, String> tokens = new HashMap<>();
      tokens.put("accessToken", newAccessToken);
      tokens.put("refreshToken", newRefreshToken);
      return tokens;
   }

   public String register(RigisterDTO rigisterDTO) {
      if (userRepository.existsByUsername(rigisterDTO.getUsername())) {
         throw new RuntimeException("Username already exists");
      }
      UserEntity userEntity = userRepository.save(userMapper.toEntity(rigisterDTO));
      if (rigisterDTO.getRole() == Role.USER) {
         userProfileRepository.save(UserProfileEntity.builder()
               .address(rigisterDTO.getAddress())
               .fullName(rigisterDTO.getFullname())
               .user(userEntity)
               .build());
      } else {
         adminProfileRepository.save(AdminProfileEntity.builder()
               .department(rigisterDTO.getDepartment())
               .user(userEntity)
               .build());
      }
      return jwtService.generateToken(userEntity);
   }

   public String login(LoginRequest request) {
      authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                  request.getUsername(),
                  request.getPassword()));
      UserEntity user = userDetailsService.loadUserByUsername(request.getUsername());
      return jwtService.generateToken(user);
   }
}
