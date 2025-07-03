package com.example.taskmanager.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.taskmanager.dto.ProjectMemberDto;
import com.example.taskmanager.enums.MembershipStatus;
import com.example.taskmanager.exception.ForbiddenException;
import com.example.taskmanager.exception.MembershipNotfoundException;
import com.example.taskmanager.exception.ProjectNotFoundException;
import com.example.taskmanager.exception.UserNotFoundException;
import com.example.taskmanager.model.ProjectEntity;
import com.example.taskmanager.model.ProjectMembershipEntity;
import com.example.taskmanager.model.UserEntity;
import com.example.taskmanager.repository.ProjectMembershipRepository;
import com.example.taskmanager.repository.ProjectRepository;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.utils.AuthUtil;

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

    private UserEntity getUserOrThrow(String username) {
        return userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException("User with username: " + username + " not found"));
    }

    private boolean isAlreadyMemberOrInvited(Long projectId, String username) {
        return projectMembershipRepository.existsByProject_IdAndUser_Username(projectId, username);
    }

    private ProjectMembershipEntity getMembershipOrThrow(Long porjectMembershipId) {
        return projectMembershipRepository.findById(porjectMembershipId)
                .orElseThrow(() -> new MembershipNotfoundException("No membership found"));
    }

    public String invite(Long projectId, String username) {
        if (isAlreadyMemberOrInvited(projectId, username)) {
            return "User is already invited or is a member of the project.";
        }
        ProjectEntity project = getProjectOrThrow(projectId);
        UserEntity user = getUserOrThrow(username);
        projectMembershipRepository.save(ProjectMembershipEntity.builder()
                .user(user)
                .project(project)
                .requestedByUser(false)
                .build());
        return "Success";
    }

    public String request(Long projectId) {
        if (isAlreadyMemberOrInvited(projectId, AuthUtil.getCurrentUsername())) {
            return "User is already invited or is a member of the project.";
        }
        ProjectEntity project = getProjectOrThrow(projectId);
        UserEntity user = getUserOrThrow(AuthUtil.getCurrentUsername());
        projectMembershipRepository.save(ProjectMembershipEntity.builder()
                .user(user)
                .project(project)
                .requestedByUser(true)
                .build());
        return "Success";
    }

    private String handleMembershipAction(Long membershipId, String currentUsername, boolean isUser,
            MembershipStatus newStatus) {
        ProjectMembershipEntity membership = getMembershipOrThrow(membershipId);
        boolean isInvite = !membership.getRequestedByUser();
        boolean isRequest = membership.getRequestedByUser();
        if ((isUser && !isInvite) || (!isUser && !isRequest)) {
            throw new ForbiddenException("You can't perform this action");
        }
        String actor = isUser ? membership.getUser().getUsername() : membership.getProject().getOwner().getUsername();
        if (!actor.equals(currentUsername)) {
            throw new ForbiddenException("You're not authorized to perform this action");
        }
        if (membership.getStatus() != MembershipStatus.PENDING) {
            return "Already " + membership.getStatus().name().toLowerCase() + " before";
        }
        membership.setStatus(newStatus);
        return newStatus.name().charAt(0) + newStatus.name().substring(1).toLowerCase() + " successfully";
    }

    public String userAccept(Long membershipId) {
        return handleMembershipAction(membershipId, AuthUtil.getCurrentUsername(), true, MembershipStatus.ACCEPTED);
    }

    public String userReject(Long membershipId) {
        return handleMembershipAction(membershipId, AuthUtil.getCurrentUsername(), true, MembershipStatus.REJECTED);
    }

    public String adminAccept(Long membershipId) {
        return handleMembershipAction(membershipId, AuthUtil.getCurrentUsername(), false, MembershipStatus.ACCEPTED);
    }

    public String adminReject(Long membershipId) {
        return handleMembershipAction(membershipId, AuthUtil.getCurrentUsername(), false, MembershipStatus.REJECTED);
    }

    public List<ProjectMemberDto> getAllProjectMemberships(Long projectId, int page, int size, String sortBy) {
        ProjectEntity projectEntity = projectRepository.findById(projectId).orElseThrow(
                () -> new ProjectNotFoundException("Project with ID " + projectId + " not found"));
        if (!projectEntity.getOwner().getUsername().equals(AuthUtil.getCurrentUsername())) {
            throw new ForbiddenException("You're not authorized to access this project");
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return projectMembershipRepository.getProjectMembers(projectId, pageable).toList();
    }
}
