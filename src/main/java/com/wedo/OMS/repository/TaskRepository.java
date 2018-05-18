package com.wedo.OMS.repository;

import com.wedo.OMS.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Task findAllById(Long id);
}