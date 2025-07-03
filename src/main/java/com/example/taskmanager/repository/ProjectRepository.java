package com.example.taskmanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.taskmanager.dto.ProjectResponseDTO;
import com.example.taskmanager.model.ProjectEntity;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    @Query(value = "select new com.example.taskmanager.dto.ProjectResponseDTO(p.id,p.name,p.description,p.numberOftasks) from ProjectEntity p where p.owner.username=:ownerUsername", countQuery = "select COUNT(p) from ProjectEntity p where p.owner.username=:ownerUsername")
    Page<ProjectResponseDTO> findAllByOwner_username(String username, Pageable pageable);

    @Query(value = "select new com.example.taskmanager.dto.ProjectResponseDTO(p.id,p.name,p.description,p.numberOftasks) from ProjectEntity p", countQuery = "select COUNT(p) from ProjectEntity p")
    Page<ProjectResponseDTO> getAllProjects(Pageable pageable);
}
