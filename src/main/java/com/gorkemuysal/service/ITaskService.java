package com.gorkemuysal.service;

import java.util.List;

import com.gorkemuysal.dto.TaskRequest;
import com.gorkemuysal.dto.TaskResponse;

public interface ITaskService {
	
	TaskResponse createTask(TaskRequest request, Long userId);
	
	List<TaskResponse> getAllTaskForUser(Long userId); 
	
	TaskResponse getTaskById(Long taskId, Long userId);
	
	TaskResponse updateTask(Long taskId, Long userId, TaskRequest request);
	
	void deleteTask(Long taskId, Long userId);
}
