package com.gorkemuysal.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gorkemuysal.dto.DtoUser;
import com.gorkemuysal.dto.RegisterRequest;
import com.gorkemuysal.entity.User;
import com.gorkemuysal.enums.RoleName;
import com.gorkemuysal.repository.UserRepository;

public class UserService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public DtoUser register(RegisterRequest request) {
		if (userRepository.existsByUsername(request.getUserName())) {
			throw new IllegalArgumentException("This username is already in use.");
		}
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new IllegalArgumentException("This email is already in use.");
		}

		User user = new User();
		user.setUsername(request.getUserName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(RoleName.USER);
		
		
		User dbUser = userRepository.save(user);
		
		
		return convertToDto(dbUser);
	}
	
	
	private DtoUser convertToDto(User user) {
		
		DtoUser dto = new DtoUser();
		BeanUtils.copyProperties(user, dto);
		
		
		return dto;
	}

}
