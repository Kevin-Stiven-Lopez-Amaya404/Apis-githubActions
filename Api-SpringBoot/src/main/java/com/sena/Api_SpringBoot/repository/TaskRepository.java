package com.sena.Api_SpringBoot.repository;

import com.sena.Api_SpringBoot.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}