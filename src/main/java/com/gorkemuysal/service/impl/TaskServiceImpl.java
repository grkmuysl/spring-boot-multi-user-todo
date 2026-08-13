package com.gorkemuysal.service.impl;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gorkemuysal.dto.DtoTask;
import com.gorkemuysal.dto.DtoUser;
import com.gorkemuysal.entity.Task;
import com.gorkemuysal.entity.User;
import com.gorkemuysal.repository.TaskRepository;
import com.gorkemuysal.service.ITaskService;

@Service
public class TaskServiceImpl implements ITaskService{
	
	private final TaskRepository taskRepository;

	TaskServiceImpl(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	@Override
	public DtoTask findTaskById(Long id) {
		
		DtoTask dtoTask = new DtoTask();
		DtoUser dtoUser = new DtoUser();
		
		Optional<Task> optional = taskRepository.findById(id);
		
		if(optional.isEmpty()) {
			return null;
		}
		
		Task task = optional.get();
		User user = task.getUser();
		BeanUtils.copyProperties(task, dtoTask);
		BeanUtils.copyProperties(user, dtoUser);
		dtoTask.setUser(dtoUser);
		
		return dtoTask;
	}

	
	
}
