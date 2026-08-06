package com.gorkemuysal.service;

import com.gorkemuysal.dto.DtoUser;
import com.gorkemuysal.dto.RegisterRequest;
import com.gorkemuysal.entity.User;

public interface IUserService {

	public DtoUser register(RegisterRequest request);
	
	private DtoUser convertToDto(User user) {
		return null;
	}
}
