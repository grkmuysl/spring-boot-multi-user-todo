package com.gorkemuysal.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.gorkemuysal.dto.DtoTask;
import com.gorkemuysal.dto.DtoUser;
import com.gorkemuysal.dto.TaskRequest;
import com.gorkemuysal.dto.TaskResponse;
import com.gorkemuysal.entity.Task;
import com.gorkemuysal.entity.User;
import com.gorkemuysal.repository.TaskRepository;
import com.gorkemuysal.repository.UserRepository;
import com.gorkemuysal.service.ITaskService;

@Service
public class TaskServiceImpl implements ITaskService {

	private final TaskRepository taskRepository;
	private final UserRepository userRepository;

	TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
		this.taskRepository = taskRepository;
		this.userRepository = userRepository;
	}

	@Override
	public TaskResponse createTask(TaskRequest request, Long userId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

		Task task = new Task();
		task.setDescription(request.getDescription());
		task.setDueDate(request.getDueDate());
		task.setStatus(request.getStatus());
		task.setTitle(request.getTitle());
		task.setUser(user);

		Task saved = taskRepository.save(task);
		return mapToResponse(task);

	}

	@Override
	public List<TaskResponse> getAllTaskForUser(Long userId) {

		List<TaskResponse> reponseList = new ArrayList<>();
		List<Task> taskList = taskRepository.findByUserId(userId);

		if (taskList.isEmpty())
			return null;

		for (Task task : taskList) {
			reponseList.add(mapToResponse(task));
		}

		return reponseList;
	}

	@Override
	public TaskResponse getTaskById(Long taskId, Long userId) {

		Optional<Task> optional = taskRepository.findByIdAndUserId(taskId, userId);

		if (!optional.isPresent())
			return null;

		return mapToResponse(optional.get());
	}

	@Override
	public TaskResponse updateTask(Long taskId, Long userId, TaskRequest request) {

		Task task = taskRepository.findByIdAndUserId(taskId, userId)
				.orElseThrow(() -> new AccessDeniedException("This task does not belong to you or was not found."));

		task.setDescription(request.getDescription());
		task.setDueDate(request.getDueDate());
		task.setStatus(request.getStatus());
		task.setTitle(request.getTitle());

		Task updated = taskRepository.save(task);

		return mapToResponse(updated);
	}

	@Override
	public void deleteTask(Long taskId, Long userId) {
		
		Task task = taskRepository.findByIdAndUserId(taskId, userId)
				.orElseThrow(() -> new AccessDeniedException("This task does not belong to you or was not found."));

		taskRepository.delete(task);
		
	}
	
	private TaskResponse mapToResponse(Task task) {
		TaskResponse response = new TaskResponse();
		response.setId(task.getId());
		response.setTitle(task.getTitle());
		response.setDescription(task.getDescription());
		response.setStatus(task.getStatus());
		response.setDueDate(task.getDueDate());
		return response;
	}

	

}
