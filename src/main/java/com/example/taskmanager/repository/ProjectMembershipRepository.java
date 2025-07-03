package com.example.taskmanager.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.taskmanager.dto.ProjectMemberDto;
import com.example.taskmanager.model.ProjectMembershipEntity;

@Repository
public interface ProjectMembershipRepository extends JpaRepository<ProjectMembershipEntity, Long> {

    boolean existsByProject_IdAndUser_Username(Long projectId, String username);

    Optional<ProjectMembershipEntity> findByProject_IdAndUser_Username(Long projectId, String username);

    @Query(value = "SELECT new com.example.taskmanager.dto.ProjectMemberDto(" +
            "u.username, u.email, up.fullName, p.status, p.requestedByUser) " +
            "FROM ProjectMembershipEntity p " +
            "JOIN p.user u " +
            "LEFT JOIN u.userprofile up " +
            "WHERE p.project.id = :projectId", countQuery = "SELECT COUNT(p) FROM ProjectMembershipEntity p WHERE p.project.id = :projectId")
    Page<ProjectMemberDto> getProjectMembers(Long projectId, Pageable pageable);
}
