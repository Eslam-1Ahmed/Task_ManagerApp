package com.example.taskmanager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.example.taskmanager.enums.TaskStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 100)
    private String title;

    @Column(length = 200)
    private String description;

    @Column(nullable = false)
    private Integer priority;

    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private String ownerUsername;

    @Column
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.TODO;

    @ManyToOne
    @JoinColumn(name = "assigneeUsername")
    private UserEntity assignee;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity project;
}
