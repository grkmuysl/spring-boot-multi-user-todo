package com.gorkemuysal.dto;

import com.gorkemuysal.enums.RoleName;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoUser {
	private Long id;

	private String username;

	private String email;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	private RoleName role;
}
