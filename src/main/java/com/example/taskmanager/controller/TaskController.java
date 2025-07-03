package com.example.taskmanager.controller;

import com.example.taskmanager.dto.TaskRequestDto;
import com.example.taskmanager.dto.TaskResponseDto;
import com.example.taskmanager.enums.TaskStatus;
import com.example.taskmanager.model.TaskEntity;
import com.example.taskmanager.response.ApiResponse;
import com.example.taskmanager.services.TaskService;
import com.example.taskmanager.utils.ValidationUtils;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

        @Autowired
        private TaskService taskService;

        @PostMapping("/add")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<?>> addTask(
                        @RequestBody @Valid TaskRequestDto taskRequestDto,
                        BindingResult bindingResult) {
                if (bindingResult.hasErrors()) {
                        ValidationUtils.handleValidationErrors(bindingResult);
                }
                TaskResponseDto savedTask = taskService.addTask(taskRequestDto);
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(new ApiResponse<>(true, "Task added successfully", savedTask));
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<TaskResponseDto>> getTaskById(@PathVariable Long id) {
                TaskResponseDto task = taskService.getTaskById(id);
                return ResponseEntity.ok(new ApiResponse<>(true, "Task retrieved successfully", task));
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<?>> deleteTask(@PathVariable Long id) {
                taskService.deleteTask(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                                .body(new ApiResponse<>(true, "Task deleted successfully", null));
        }

        @PutMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<?>> updateTask(
                        @PathVariable Long id,
                        @RequestBody @Valid TaskEntity task,
                        BindingResult bindingResult) {
                if (bindingResult.hasErrors()) {
                        ValidationUtils.handleValidationErrors(bindingResult);
                }
                TaskResponseDto updatedTask = taskService.updateTask(id, task);
                return ResponseEntity.ok(new ApiResponse<>(true, "Task updated successfully", updatedTask));
        }

        @GetMapping("/my")
        public ResponseEntity<ApiResponse<List<TaskResponseDto>>> getMyTasks(
                        @RequestParam int page,
                        @RequestParam int size,
                        @RequestParam(defaultValue = "id") String sortBy) {
                List<TaskResponseDto> tasks = taskService.getMyTasks(page, size, sortBy);
                return ResponseEntity.ok(new ApiResponse<>(true, "My tasks retrieved successfully", tasks));
        }

        @GetMapping("/project/{projectId}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<List<TaskResponseDto>>> getTasksByProjectId(
                        @PathVariable Long projectId,
                        @RequestParam int page,
                        @RequestParam int size,
                        @RequestParam(defaultValue = "id") String sortBy) {
                List<TaskResponseDto> tasks = taskService.getTasksByProjectId(projectId, page, size, sortBy);
                return ResponseEntity
                                .ok(new ApiResponse<>(true, "Tasks with this project retrieved successfully", tasks));
        }

        @PutMapping("/{id}/status/{taskStatus}")
        public ResponseEntity<ApiResponse<?>> changeTaskStatus(
                        @PathVariable Long id,
                        @PathVariable TaskStatus taskStatus) {
                String result = taskService.changeTaskStatus(id, taskStatus);
                return ResponseEntity.ok(new ApiResponse<>(true, result, null));
        }

        @PutMapping("/{id}/assign/{username}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ApiResponse<?>> assignTask(
                        @PathVariable Long id,
                        @PathVariable String username) {
                String result = taskService.assignTask(id, username);
                return ResponseEntity.ok(new ApiResponse<>(true, result, null));
        }
}
