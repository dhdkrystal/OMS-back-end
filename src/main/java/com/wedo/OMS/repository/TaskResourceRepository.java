package com.wedo.OMS.repository;

import com.wedo.OMS.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskResourceRepository extends JpaRepository<Task, Long> {
}