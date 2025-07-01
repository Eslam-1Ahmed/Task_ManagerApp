package com.example.taskmanager.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.taskmanager.dto.UserDTO;
import com.example.taskmanager.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDTO> getAllUsers(int size,int page,String sortBy){

        Pageable pageable = PageRequest.of(page, size,Sort.by(sortBy));
        Page<UserDTO> result = userRepository.getAllUsers(pageable);
        return result.stream().toList();
    }
}
