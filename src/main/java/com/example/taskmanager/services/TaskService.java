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
                .orElseThrow(
                        () -> new ProjectNotFoundException(
                                "Project with ID " + taskRequestDto.getProjectId() + " not found"));
        if (!projectEntity.getOwner().getId().equals(taskRequestDto.getOwnerId())) {
            throw new ForbiddenException("This User Can't Access This project");
        }
        return taskMapper.toDto(taskRepository.save(taskMapper.toEntityFromRequest(taskRequestDto)));
    }

    public List<TaskResponseDto> getAllTasks() {
        taskRepository.findAll().stream().map(taskMapper::toDto).toList();
        return taskRepository.findAll().stream().map(taskMapper::toDto).toList();
    }

    public String assigntask(Long id, Long userId, Long adminId) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " not found"));
        Long projecId = task.getProject().getId();
        if (task.getOwnerId().equals(adminId)) {
            throw new ForbiddenException("This User Can't Access This project");
        }
        if (!projectMembershipRepository.existsByProject_IdAndUser_Id(projecId, userId)) {
            throw new ForbiddenException("This User Not a member in this project");
        }
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
        task.setAssignee(user);
        taskRepository.save(task);

        return "Task assigned successfully to user ID " + userId;
    }

    public TaskResponseDto getTaskById(Long id) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with ID " + id + " not found"));
        return taskMapper.toDto(task);
    }

    public List<TaskResponseDto> getTaskByuserId(Long userId, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<TaskResponseDto> task = taskRepository.findByUser_Id(userId, pageable);
        return task.toList();
    }

    public List<TaskResponseDto> getTaskByprojectId(Long projectId, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<TaskResponseDto> task = taskRepository.findByProject_Id(projectId, pageable);
        return task.toList();
    }

    public void deleteTask(Long id, Long userId) {
        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(
                () -> new TaskNotFoundException("Task with ID " + id + " not found"));
        if (!taskEntity.getOwnerId().equals(userId)) {
            throw new ForbiddenException("This user can't access this task");
        }
        taskRepository.deleteById(id);
    }

    public TaskResponseDto updateTask(Long id, Long userId, TaskEntity updatedTask) {
        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(
                () -> new TaskNotFoundException("Task with ID " + id + " not found"));
        if (!taskEntity.getOwnerId().equals(userId)) {
            throw new ForbiddenException("This User Can't Access This task");
        }
        return taskMapper.toDto(taskRepository.save(updatedTask));
    }

    public String changeTaskStatus(Long id, Long userId, TaskStatus taskStatus) {
        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(
                () -> new TaskNotFoundException("Task with ID " + id + " not found"));
        if (!taskEntity.getOwnerId().equals(userId) && taskEntity.getAssignee().getId().equals(userId)) {
            throw new ForbiddenException("This user can't access this task");
        }
        taskEntity.setStatus(taskStatus);
        taskRepository.save(taskEntity);
        return "Status changed successfuly";
    }

    public List<TaskResponseDto> getPagedAndSortedTasks(int page, int size, String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<TaskEntity> pageResult = taskRepository.findAll(pageable);
        return pageResult.stream().map(taskMapper::toDto).toList();
    }
}
