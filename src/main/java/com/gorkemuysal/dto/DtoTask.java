package com.gorkemuysal.dto;

import java.util.Date;

import com.gorkemuysal.entity.User;
import com.gorkemuysal.enums.TaskStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoTask {
	
	private Long id;
	
	private String title;
	
	private String description;
	
	@Enumerated(EnumType.STRING)
	private TaskStatus status;
	
	private Date dueDate;
	
	@ManyToOne
	private DtoUser user;
}
