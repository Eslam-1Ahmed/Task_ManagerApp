package com.example.taskmanager.mapper;

import org.springframework.stereotype.Component;

import com.example.taskmanager.dto.TaskRequestDto;
import com.example.taskmanager.dto.TaskResponseDto;
import com.example.taskmanager.model.TaskEntity;

@Component
public class TaskMapper {

    public TaskResponseDto toDto(TaskEntity task) {
        TaskResponseDto dto = new TaskResponseDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        return dto;
    }
    public TaskEntity toEntityFromRequest(TaskRequestDto taskRequestDto) {
        TaskEntity task = TaskEntity.builder().build();
        task.setTitle(taskRequestDto.getTitle());
        task.setDescription(taskRequestDto.getDescription());
        task.setPriority(taskRequestDto.getPriority());
        return task;
    }
}
