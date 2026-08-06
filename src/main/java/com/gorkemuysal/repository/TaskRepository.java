package com.gorkemuysal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gorkemuysal.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{

}
