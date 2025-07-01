package com.example.taskmanager.mapper;


import org.springframework.stereotype.Component;
import com.example.taskmanager.dto.ProjectRequestDTO;
import com.example.taskmanager.model.ProjectEntity;
import com.example.taskmanager.model.UserEntity;

@Component
public class ProjectMapper {
    public ProjectEntity toEntityFromRequest(ProjectRequestDTO projectRequestDTO, UserEntity owner) {
        ProjectEntity projectEntity = ProjectEntity.builder().name(projectRequestDTO.getName()
        ).description(projectRequestDTO.getDescription()
        ).owner(owner).build();

        return projectEntity;
    }
}
