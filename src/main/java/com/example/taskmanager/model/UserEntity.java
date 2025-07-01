package com.example.taskmanager.model;

import java.util.ArrayList;
import java.util.List;

import com.example.taskmanager.enums.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String username;

    @Column
    @Email
    private String email;

    @Column
    @Size(min = 8)
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    
    private UserProfileEntity userprofile;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    
    private AdminProfileEntity adminprofile;

    @OneToMany(mappedBy = "assignee")
    @Builder.Default
    private List<TaskEntity> assignedTasks = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    @Builder.Default
    List<ProjectMembershipEntity> memberships = new ArrayList<>();
}
