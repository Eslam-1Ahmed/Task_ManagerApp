package com.example.taskmanager.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.taskmanager.dto.TaskRequestDto;
import com.example.taskmanager.dto.TaskResponseDto;
import com.example.taskmanager.enums.TaskStatus;
import com.example.taskmanager.exception.ForbiddenException;
import com.example.taskmanager.exception.ProjectNotFoundException;
import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.exception.UserNotFoundException;
import com.example.taskmanager.mapper.TaskMapper;
import com.example.taskmanager.model.ProjectEntity;
import com.example.taskmanager.model.TaskEntity;
import com.example.taskmanager.model.UserEntity;
import com.example.taskmanager.repository.ProjectMembershipRepository;
import com.example.taskmanager.repository.ProjectRepository;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.repository.UserRepository;
import com.example.taskmanager.utils.AuthUtil;

@Service
public class TaskService {

    private final ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectMembershipRepository projectMembershipRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskMapper taskMapper;

    TaskService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public TaskResponseDto addTask(TaskRequestDto taskRequestDto) {
        ProjectEntity projectEntity = projectRepository.findById(taskRequestDto.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException(
                        "Project with ID " + taskRequestDto.getProjectId() + " not found"));

        if (!projectEntity.getOwner().getUsername().equals(AuthUtil.getCurrentUsername())) {
            throw new ForbiddenException("Only the project owner can add tasks");
        }

        TaskEntity task = taskMapper.toEntityFromRequest(taskRequestDto);
        return taskMapper.toDto(taskRepository.save(task));
    }

    public String assignTask(Long taskId, String username) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + taskId + " not found"));

        if (!task.getOwnerUsername().equals(AuthUtil.getCurrentUsername())) {
            throw new ForbiddenException("Only the task owner can assign this task");
        }
        Long projectId = task.getProject().getId();
        if (!projectMembershipRepository.existsByProject_IdAndUser_Username(projectId, username)) {
            throw new ForbiddenException("The user is not a member of this project");
        }
        UserEntity user = userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
        task.setAssignee(user);
        taskRepository.save(task);
        return "Task assigned successfully to user: " + username;
    }

    public TaskResponseDto getTaskById(Long taskId) {
        TaskEntity task = getTaskIfAuthorized(taskId);
        return taskMapper.toDto(task);
    }

    public List<TaskResponseDto> getMyTasks(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<TaskResponseDto> tasks = taskRepository.findByUsername(AuthUtil.getCurrentUsername(), pageable);
        return tasks.toList();
    }

    public List<TaskResponseDto> getTasksByProjectId(Long projectId, int page, int size, String sortBy) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project with ID " + projectId + " not found"));

        if (!project.getOwner().getUsername().equals(AuthUtil.getCurrentUsername())) {
            throw new ForbiddenException("Only the project owner can view its tasks");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<TaskResponseDto> tasks = taskRepository.findByProject_Id(projectId, pageable);
        return tasks.toList();
    }

    public void deleteTask(Long taskId) {
        TaskEntity task = getTaskIfOwner(taskId);
        taskRepository.delete(task);
    }

    public TaskResponseDto updateTask(Long taskId, TaskEntity updatedTask) {
        TaskEntity task = getTaskIfOwner(taskId);
        updatedTask.setId(task.getId());
        return taskMapper.toDto(taskRepository.save(updatedTask));
    }

    public String changeTaskStatus(Long taskId, TaskStatus status) {
        TaskEntity task = getTaskIfAuthorized(taskId);
        task.setStatus(status);
        taskRepository.save(task);
        return "Task status updated to " + status.name().toLowerCase();
    }

    private TaskEntity getTaskIfAuthorized(Long taskId) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + taskId + " not found"));

        String currentUsername = AuthUtil.getCurrentUsername();
        boolean isAssignee = task.getAssignee() != null &&
                task.getAssignee().getUsername().equals(currentUsername);
        boolean isOwner = task.getOwnerUsername().equals(currentUsername);

        if (!isAssignee && !isOwner) {
            throw new ForbiddenException("You're not authorized to access this task");
        }

        return task;
    }

    private TaskEntity getTaskIfOwner(Long taskId) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + taskId + " not found"));
        if (!task.getOwnerUsername().equals(AuthUtil.getCurrentUsername())) {
            throw new ForbiddenException("Only the task owner can perform this operation");
        }
        return task;
    }
}
