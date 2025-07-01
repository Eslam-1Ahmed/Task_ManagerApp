package com.example.taskmanager.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ApiResponse<?>> addProject(@RequestBody @Valid ProjectRequestDTO projectRequestDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            HashMap<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Validation Errors", errors));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(false, projectService.addProject(projectRequestDTO), null));
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<?>> findAllProjects(@RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy) {
        return ResponseEntity.ok().body(new ApiResponse<>(true, "Success", projectService.findAllProjects(
                page, size, sortBy)));
    }

    @GetMapping("/owned/{ownerId}")
    public ResponseEntity<ApiResponse<?>> findAllProjects(@PathVariable Long ownerId, @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sortBy) {
        return ResponseEntity.ok()
                .body(new ApiResponse<>(true, "Success",
                        projectService.findAllProjects(ownerId, page, size, sortBy)));
    }

}
