package com.example.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.taskmanager.model.AdminProfileEntity;

@Repository
public interface AdminProfileRepository extends JpaRepository<AdminProfileEntity,Long> {

}
