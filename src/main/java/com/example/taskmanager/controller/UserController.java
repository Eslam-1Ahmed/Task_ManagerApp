package com.example.taskmanager.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.taskmanager.response.ApiResponse;
import com.example.taskmanager.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllUsers(@RequestParam int size, @RequestParam int page,
            @RequestParam String sortBy) {
        return ResponseEntity.ok().body(new ApiResponse<>(true, "Success",
                userService.getAllUsers(size, page, sortBy)));
    }
}
