package com.example.taskmanager.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.taskmanager.dto.UserDTO;
import com.example.taskmanager.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("select new com.example.taskmanager.dto.UserDTO(u.id,u.username) from UserEntity u")
    public Page<UserDTO> getAllUsers(Pageable pageable);

    Optional<UserEntity> findByUsername(String username);
    boolean existsByUsername(String username);
}
