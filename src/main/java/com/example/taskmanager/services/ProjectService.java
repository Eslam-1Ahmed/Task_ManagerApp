package com.example.taskmanager.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.taskmanager.dto.ProjectRequestDTO;
import com.example.taskmanager.dto.ProjectResponseDTO;
import com.example.taskmanager.execptions.UserNotFoundException;
import com.example.taskmanager.mapper.ProjectMapper;
import com.example.taskmanager.model.ProjectEntity;
import com.example.taskmanager.model.UserEntity;
import com.example.taskmanager.repository.ProjectRepository;
import com.example.taskmanager.repository.UserRepository;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectMapper projectMapper;

    public String addProject(ProjectRequestDTO projectRequestDTO) {
        UserEntity owner = userRepository.findById(projectRequestDTO.getOwnerId())
                .orElseThrow(() -> new UserNotFoundException(
                        "User Not Found"));
        ProjectEntity project = projectMapper.toEntityFromRequest(projectRequestDTO, owner);
        projectRepository.save(project);
        return "Added Successfully";
    }

    public List<ProjectResponseDTO> findAllProjects(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return projectRepository.getAllProjects(pageable).toList();
    }

    public List<ProjectResponseDTO> findAllProjects(Long ownerId, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return projectRepository.findAllByOwnerId(ownerId, pageable).toList();
    }
}
