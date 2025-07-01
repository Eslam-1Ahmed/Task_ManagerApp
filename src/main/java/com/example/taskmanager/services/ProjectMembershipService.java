package com.example.taskmanager.services;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.taskmanager.dto.ProjectMemberDto;
import com.example.taskmanager.enums.MembershipStatus;
import com.example.taskmanager.execptions.MembershipNotfoundException;
import com.example.taskmanager.execptions.ProjectNotFoundException;
import com.example.taskmanager.execptions.UserNotFoundException;
import com.example.taskmanager.model.ProjectEntity;
import com.example.taskmanager.model.ProjectMembershipEntity;
import com.example.taskmanager.model.UserEntity;
import com.example.taskmanager.repository.ProjectMembershipRepository;
import com.example.taskmanager.repository.ProjectRepository;
import com.example.taskmanager.repository.UserRepository;

@Service
public class ProjectMembershipService {

    @Autowired
    private ProjectMembershipRepository projectMembershipRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;

    private ProjectEntity getProjectOrThrow(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + projectId + " not found"));
    }

    private UserEntity getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
    }

    private boolean isAlreadyMemberOrInvited(Long projectId, Long userId) {
        return projectMembershipRepository.existsByProject_IdAndUser_Id(projectId, userId);
    }

    private ProjectMembershipEntity getMembershipOrThrow(Long projectId, Long userId) {
        return projectMembershipRepository.findByProject_IdAndUser_Id(projectId, userId)
                .orElseThrow(() -> new MembershipNotfoundException("No membership found"));
    }

    public String invite(Long projectId, Long userId) {
        if (isAlreadyMemberOrInvited(projectId, userId)) {
            return "User is already invited or is a member of the project.";
        }
        ProjectEntity project = getProjectOrThrow(projectId);
        UserEntity user = getUserOrThrow(userId);
        projectMembershipRepository.save(ProjectMembershipEntity.builder()
                .user(user)
                .project(project)
                .requestedByUser(false)
                .build());
        return "Success";
    }

    public String request(Long projectId, Long userId) {
        if (isAlreadyMemberOrInvited(projectId, userId)) {
            return "User is already invited or is a member of the project.";
        }
        ProjectEntity project = getProjectOrThrow(projectId);
        UserEntity user = getUserOrThrow(userId);
        projectMembershipRepository.save(ProjectMembershipEntity.builder()
                .user(user)
                .project(project)
                .requestedByUser(true)
                .build());
        return "Success";
    }

    public String accept(Long projectId, Long userId) {
        ProjectMembershipEntity projectMembershipEntity = getMembershipOrThrow(projectId, userId);
        projectMembershipEntity.setStatus(MembershipStatus.ACCEPTED);
        projectMembershipRepository.save(projectMembershipEntity);
        return "Success";
    }

    public String reject(Long projectId, Long userId) {
        ProjectMembershipEntity projectMembershipEntity = getMembershipOrThrow(projectId, userId);
        projectMembershipEntity.setStatus(MembershipStatus.REJECTED);
        projectMembershipRepository.save(projectMembershipEntity);
        return "Success";
    }

    public List<ProjectMemberDto> getAllProjectMemberships(Long projectId,int page,int size,String sortBy) {
        if (!projectRepository.existsById(projectId)) {
            throw new ProjectNotFoundException("Project with ID " + projectId + " not found");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return projectMembershipRepository.getProjectMembers(pageable,projectId).toList();
    }
}
