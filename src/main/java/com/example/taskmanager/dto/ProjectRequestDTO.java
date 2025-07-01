package com.example.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequestDTO {

   @NotBlank(message = "The name can't empty")
   private String name;
   @NotBlank(message = "The description can't empty")
   private String description;

   @NotNull(message = "The ownerId is requierd")
   private Long ownerId;
}
