package com.gorkemuysal.controller.impl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gorkemuysal.controller.ITaskController;
import com.gorkemuysal.dto.DtoTask;
import com.gorkemuysal.service.ITaskService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/task")
public class TaskControllerImpl implements ITaskController {

	private final ITaskService taskService;

	TaskControllerImpl(ITaskService taskService) {
		this.taskService = taskService;
	}

	@GetMapping("/list/{id}")
	@Override
	public DtoTask findTaskById(@Valid @NotEmpty @PathVariable(value = "id") Long id) {
		return taskService.findTaskById(id);

	}

}
