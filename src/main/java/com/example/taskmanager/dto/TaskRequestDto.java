package com.example.taskmanager.dto;

import com.example.taskmanager.enums.TaskStatus;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDto {

    @NotNull(message = "Project Id is requierd")
    private Long projectId;

    @Nullable
    private Long assignedId;

    @NotBlank(message = "Title is required")
    @Size(min = 3, message = "Title must be at least 3 characters")
    private String title;

    @Size(max = 250, message = "Description must be less than 250 characters")
    private String description;

    @Min(value = 1, message = "priority must be atleast 1")
    private Integer priority;

    @NotNull(message = "Price is requierd")
    private Double price;
    
    @NotNull(message = "Owner Id is requierd")
    private Long ownerId;

    private TaskStatus status = TaskStatus.TODO;

}