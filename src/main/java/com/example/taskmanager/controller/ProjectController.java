package com.example.taskmanager.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanager.constants.ApiMessages;
import com.example.taskmanager.dto.ProjectRequestDTO;
import com.example.taskmanager.response.ApiResponse;
import com.example.taskmanager.services.ProjectService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> addProject(@RequestBody @Valid ProjectRequestDTO projectRequestDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            HashMap<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, ApiMessages.VALIDATION_ERRORS, errors));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(true, "Project added successfully", projectService.addProject(projectRequestDTO)));
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<?>> findAllProjects(@RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy) {
        return ResponseEntity.ok().body(new ApiResponse<>(true, "Success", projectService.findAllProjects(
                page, size, sortBy)));
    }

    @GetMapping("/get/myprojects")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<?>> findMyProjects(@RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy) {
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Success",
                        projectService.findMyProjects(page, size, sortBy)));
    }

}
