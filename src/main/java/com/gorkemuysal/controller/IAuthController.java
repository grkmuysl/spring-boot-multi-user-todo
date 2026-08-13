package com.gorkemuysal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.gorkemuysal.dto.RegisterRequest;
import com.gorkemuysal.jwt.AuthResponse;

public interface IAuthController {

	public ResponseEntity<?> register(@RequestBody RegisterRequest request);
	
	public AuthResponse authenticate(@RequestBody RegisterRequest request);
}
