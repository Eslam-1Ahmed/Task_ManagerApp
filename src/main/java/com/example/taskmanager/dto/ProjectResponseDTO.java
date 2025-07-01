package com.example.taskmanager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponseDTO {

    @NotBlank(message = "The name can't empty")
    private String name;
    @NotBlank(message = "The description can't empty")
    private String description;
    @Min(value = 0)
    @NotNull(message = "numberOftasks can't be empty")
    private Integer numberOftasks;
}
