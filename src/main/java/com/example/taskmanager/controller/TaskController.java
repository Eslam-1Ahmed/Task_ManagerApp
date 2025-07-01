package com.example.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanager.dto.TaskRequestDto;
import com.example.taskmanager.dto.TaskResponseDto;
import com.example.taskmanager.enums.TaskStatus;
import com.example.taskmanager.model.TaskEntity;
import com.example.taskmanager.response.ApiResponse;
import com.example.taskmanager.services.TaskService;
import com.example.taskmanager.utiltis.ValidationUtils;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

        @Autowired
        private TaskService taskService;

        @PostMapping("/add")
        public ResponseEntity<ApiResponse<?>> addTask(
                        @RequestBody @Valid TaskRequestDto taskRequestDto,
                        BindingResult bindingResult) {
                if (bindingResult.hasErrors()) {
                        ValidationUtils.handleValidationErrors(bindingResult);
                }
                TaskResponseDto savedTask = taskService.addTask(taskRequestDto);
                return ResponseEntity.status(HttpStatus.CREATED).body(
                                new ApiResponse<>(true, "Task added successfully", savedTask));
        }

        @GetMapping
        public ResponseEntity<ApiResponse<List<TaskResponseDto>>> getAllTasks() {
                List<TaskResponseDto> tasks = taskService.getAllTasks();
                return ResponseEntity.ok(
                                new ApiResponse<>(true, "Tasks retrieved successfully", tasks));
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<TaskResponseDto>> getTaskById(@PathVariable Long id) {
                TaskResponseDto task = taskService.getTaskById(id);
                return ResponseEntity.ok(
                                new ApiResponse<>(true, "Task retrieved successfully", task));
        }

        @DeleteMapping("/{id}/user/{userId}")
        public ResponseEntity<ApiResponse<Object>> deleteTask(@PathVariable Long id, @PathVariable Long userId) {
                taskService.deleteTask(id, userId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                                new ApiResponse<>(true, "Task deleted successfully", null));
        }

        @PutMapping("/{id}/user/{userId}")
        public ResponseEntity<ApiResponse<?>> updateTask(
                        @PathVariable Long id, @PathVariable Long userId, @RequestBody @Valid TaskEntity task,
                        BindingResult bindingResult) {
                if (bindingResult.hasErrors()) {
                        ValidationUtils.handleValidationErrors(bindingResult);
                }
                TaskResponseDto updatedTask = taskService.updateTask(id, userId, task);
                return ResponseEntity.ok(
                                new ApiResponse<>(true, "Task updated successfully", updatedTask));
        }

        @GetMapping("/page")
        public ResponseEntity<ApiResponse<List<TaskResponseDto>>> getTasks(
                        @RequestParam int page,
                        @RequestParam int size,
                        @RequestParam(defaultValue = "id") String sortBy) {

                List<TaskResponseDto> tasks = taskService.getPagedAndSortedTasks(page, size, sortBy);
                return ResponseEntity.ok(new ApiResponse<>(true, "", tasks));
        }

        @GetMapping("/user/{userId}")
        public List<TaskResponseDto> getTaskByuserId(@PathVariable Long userId,
                        @RequestParam int page, @RequestParam int size,
                        @RequestParam(defaultValue = "id") String sortBy) {
                return taskService.getTaskByuserId(userId, page, size, sortBy);
        }

        @GetMapping("/project/{projectId}")
        public ApiResponse<List<TaskResponseDto>> getTaskByprojectId(@PathVariable Long projectId,
                        @RequestParam int page, @RequestParam int size,
                        @RequestParam(defaultValue = "id") String sortBy) {
                return new ApiResponse<List<TaskResponseDto>>(true,
                                "Tasks With This project retrived Successfully",
                                taskService.getTaskByuserId(projectId, page, size, sortBy));
        }

        @PutMapping("/{id}/user/{userId}/status/{taskStatus}")
        public ApiResponse<?> changeTaskStatus(@PathVariable Long id, @PathVariable Long userId,
                        @PathVariable TaskStatus taskStatus) {
                return new ApiResponse<>(true, taskService.changeTaskStatus(id, userId, taskStatus), null);
        }

        @PutMapping("/{id}/user/{userId}/admin/{adminId}")
        public ApiResponse<?> assigntask(@PathVariable Long id, @PathVariable Long userId, @PathVariable Long adminId) {
                return new ApiResponse<>(true, taskService.assigntask(id, userId, adminId), null);
        }
}