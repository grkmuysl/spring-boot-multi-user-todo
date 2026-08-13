package com.gorkemuysal.controller.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gorkemuysal.controller.IAuthController;
import com.gorkemuysal.dto.DtoUser;
import com.gorkemuysal.dto.RegisterRequest;
import com.gorkemuysal.service.IUserService;



@RestController
public class AuthController implements IAuthController {

	private final IUserService userService;

	public AuthController(IUserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	@Override
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

		DtoUser savedUser = userService.register(request);
		
		return ResponseEntity.ok("Register successfully: " + savedUser.getUsername());
	}

}
