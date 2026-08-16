package com.gorkemuysal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gorkemuysal.entity.Task;
import com.gorkemuysal.enums.TaskStatus;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{

	Page<Task> findByUserId(Long userId , Pageable pageable);
	
	
	Optional<Task> findByIdAndUserId(Long id, Long userId);
	
	List<Task> findByUserIdAndStatus(Long userId, TaskStatus status);
	
}
