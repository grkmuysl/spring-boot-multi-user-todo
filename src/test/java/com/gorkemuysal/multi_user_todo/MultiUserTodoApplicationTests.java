package com.gorkemuysal.multi_user_todo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gorkemuysal.dto.TaskResponse;
import com.gorkemuysal.service.ITaskService;

@SpringBootTest(classes = { MultiUserTodoApplication.class })
class MultiUserTodoApplicationTests {

	@Autowired
	private ITaskService taskService;

	@Test
	public void testGetTaskById() {
		TaskResponse response = taskService.getTaskById(2L, 15L);

		if (response != null) {
			System.out.println("TASK NAME IS:" + response.getTitle());
		}
	}
}
