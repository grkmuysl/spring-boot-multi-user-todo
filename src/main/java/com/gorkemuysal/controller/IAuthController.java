package com.gorkemuysal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.gorkemuysal.dto.RegisterRequest;

public interface IAuthController {

	public ResponseEntity<?> register(@RequestBody RegisterRequest request);
}
