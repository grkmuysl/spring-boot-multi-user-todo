package com.gorkemuysal.dto;

import java.time.LocalDateTime;

import com.gorkemuysal.enums.TaskStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {

	private String title;

	private String description;

	private TaskStatus status;

	private LocalDateTime dueDate;


}
