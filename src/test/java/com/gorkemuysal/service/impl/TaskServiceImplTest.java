package com.gorkemuysal.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.gorkemuysal.dto.TaskRequest;
import com.gorkemuysal.dto.TaskResponse;
import com.gorkemuysal.entity.Task;
import com.gorkemuysal.entity.User;
import com.gorkemuysal.repository.TaskRepository;
import com.gorkemuysal.repository.UserRepository;

public class TaskServiceImplTest {
	@Mock
	private TaskRepository taskRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private TaskServiceImpl taskService;

	private User user;
	private Task task;
	private TaskRequest request;

	@BeforeEach
	void setup() {
		user = new User();

		user.setId(1L);

		task = new Task();
		task.setId(10L);
		task.setTitle("Ödevi bitir");
		task.setDescription("JUnit çalış");
		task.setUser(user);

		request = new TaskRequest();
		request.setTitle("test title");
		request.setDescription("JUnit test desc");
	}
	
	
	// createTask
	@Test
	void createTask_IfUserExists_createTask() {
		given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(taskRepository.save(any(Task.class))).willReturn(task);
 
        TaskResponse response = taskService.createTask(request, 1L);
 
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getTitle()).isEqualTo("test title");
        verify(taskRepository, times(1)).save(any(Task.class));
	}
	
	@Test
	void createTask_IfUserNotExists_throwsException() {
		 given(userRepository.findById(99L)).willReturn(Optional.empty());
		 
	        assertThrows(RuntimeException.class, () -> taskService.createTask(request, 99L));
	 
	        verify(taskRepository, never()).save(any(Task.class));
	}


}
