package com.example.taskmanager.dto;

import com.example.taskmanager.enums.MembershipStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberDto {
    private String username;
    private String email;
    private String fullName;
    private MembershipStatus status;
    private Boolean requestedByUser;
}
