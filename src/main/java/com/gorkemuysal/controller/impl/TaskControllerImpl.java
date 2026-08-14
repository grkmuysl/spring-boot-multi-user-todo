package com.gorkemuysal.controller.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gorkemuysal.controller.ITaskController;
import com.gorkemuysal.dto.TaskRequest;
import com.gorkemuysal.dto.TaskResponse;
import com.gorkemuysal.entity.UserDetailsImpl;
import com.gorkemuysal.service.ITaskService;

@RestController
@RequestMapping("/api/task")
public class TaskControllerImpl implements ITaskController {

	private final ITaskService taskService;

	TaskControllerImpl(ITaskService taskService) {
		this.taskService = taskService;
	}

	@PostMapping("/create")
	@Override
	public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {

		TaskResponse response = taskService.createTask(request, userDetails.getId());

		return ResponseEntity.ok(response);
	}

	@GetMapping("/list")
	@Override
	public ResponseEntity<List<TaskResponse>> getAllTaskForUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {

		return ResponseEntity.ok(taskService.getAllTaskForUser(userDetails.getId()));
	}

	@GetMapping("/{id}")
	@Override
	public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {

		return ResponseEntity.ok(taskService.getTaskById(id, userDetails.getId()));
	}

	@PutMapping("/{id}")
	@Override
	public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id,
			@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody TaskRequest request) {

		return ResponseEntity.ok(taskService.updateTask(id, userDetails.getId(), request));
	}

	@DeleteMapping("/{id}")
	@Override
	public ResponseEntity<Void> deleteTask(@PathVariable Long id,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {

		taskService.deleteTask(id, userDetails.getId());
		return ResponseEntity.noContent().build();
	}

}
