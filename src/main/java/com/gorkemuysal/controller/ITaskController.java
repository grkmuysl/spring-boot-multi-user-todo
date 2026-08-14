package com.gorkemuysal.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.gorkemuysal.dto.TaskRequest;
import com.gorkemuysal.dto.TaskResponse;
import com.gorkemuysal.entity.UserDetailsImpl;

public interface ITaskController {

	public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request,
			@AuthenticationPrincipal UserDetailsImpl userDetails);

	public ResponseEntity<List<TaskResponse>> getAllTaskForUser(@AuthenticationPrincipal UserDetailsImpl userDetails);

	public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long taskId,
			@AuthenticationPrincipal UserDetailsImpl userDetails);

	public ResponseEntity<TaskResponse> updateTask(@PathVariable Long taskId,
			@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody TaskRequest request);

	public ResponseEntity<Void> deleteTask(@PathVariable Long taskId,
			@AuthenticationPrincipal UserDetailsImpl userDetails);

}
