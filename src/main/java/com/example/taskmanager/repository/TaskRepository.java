package com.example.taskmanager.repository;

import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.taskmanager.dto.TaskResponseDto;
import com.example.taskmanager.model.TaskEntity;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

        public boolean existsById(Long id);

        public void deleteById(Long id);

        @Query("select new com.example.taskmanager.dto.TaskResponseDto"
                        + "(t.id,project.id,t.assignee.id,"
                        + "t.title,t.description,t.priority,t.price,t.status) from TaskEntity t where t.assignee.username=:username")
        public Page<TaskResponseDto> findByUsername(String username, Pageable pageable);

        @Query("select new com.example.taskmanager.dto.TaskResponseDto"
                        + "(t.id,project.id,t.assignee.id,"
                        + "t.title,t.description,t.priority,t.price,t.status) from TaskEntity t where t.project.id=:projectId")
        public Page<TaskResponseDto> findByProject_Id(Long projectId, Pageable pageable);

}
