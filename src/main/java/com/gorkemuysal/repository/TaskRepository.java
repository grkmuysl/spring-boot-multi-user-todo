package com.gorkemuysal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gorkemuysal.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long>{

}
