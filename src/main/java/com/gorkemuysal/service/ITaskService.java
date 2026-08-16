package com.gorkemuysal.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gorkemuysal.dto.TaskRequest;
import com.gorkemuysal.dto.TaskResponse;

public interface ITaskService {
	
	TaskResponse createTask(TaskRequest request, Long userId);
	
	Page<TaskResponse> getAllTaskForUser(Long userId, Pageable pageable); 
	
	TaskResponse getTaskById(Long taskId, Long userId);
	
	TaskResponse updateTask(Long taskId, Long userId, TaskRequest request);
	
	void deleteTask(Long taskId, Long userId);
}
