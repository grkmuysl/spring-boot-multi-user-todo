package com.gorkemuysal.service;

import org.springframework.web.bind.annotation.RequestBody;

import com.gorkemuysal.dto.DtoUser;
import com.gorkemuysal.dto.RegisterRequest;
import com.gorkemuysal.entity.User;
import com.gorkemuysal.jwt.AuthResponse;

public interface IUserService {

	public DtoUser register(RegisterRequest request);
	
	private DtoUser convertToDto(User user) {
		return null;
	}
	
	public AuthResponse authenticate(RegisterRequest request);
}
