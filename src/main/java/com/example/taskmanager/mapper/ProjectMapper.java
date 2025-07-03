package com.example.taskmanager.mapper;

import org.springframework.stereotype.Component;
import com.example.taskmanager.dto.ProjectRequestDTO;
import com.example.taskmanager.dto.ProjectResponseDTO;
import com.example.taskmanager.model.ProjectEntity;
import com.example.taskmanager.model.UserEntity;

@Component
public class ProjectMapper {
    public ProjectEntity toEntityFromRequest(ProjectRequestDTO projectRequestDTO, UserEntity owner) {
        ProjectEntity projectEntity = ProjectEntity.builder().name(projectRequestDTO.getName())
                .description(projectRequestDTO.getDescription()).owner(owner).build();
        return projectEntity;
    }

    public ProjectResponseDTO toDto(ProjectEntity projectEntity) {
        return ProjectResponseDTO.builder().id(projectEntity.getId())
                .name(projectEntity.getName()).description(projectEntity.getDescription())
                .numberOftasks(projectEntity.getNumberOftasks()).build();
    }
}
