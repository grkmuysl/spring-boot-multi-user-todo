package com.gorkemuysal.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gorkemuysal.dto.DtoUser;
import com.gorkemuysal.dto.RegisterRequest;
import com.gorkemuysal.entity.User;
import com.gorkemuysal.entity.UserDetailsImpl;
import com.gorkemuysal.enums.RoleName;
import com.gorkemuysal.jwt.AuthResponse;
import com.gorkemuysal.jwt.JwtService;
import com.gorkemuysal.repository.UserRepository;
import com.gorkemuysal.service.IUserService;

@Service
public class UserService implements IUserService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final AuthenticationProvider authenticationProvider;

	private final JwtService jwtService;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
			AuthenticationProvider authenticationProvider, JwtService jwtService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationProvider = authenticationProvider;
		this.jwtService = jwtService;
	}

	@Override
	public AuthResponse authenticate(RegisterRequest request) {

		try {
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(request.getUsername(),
					request.getPassword());
			authenticationProvider.authenticate(auth);

			User user = userRepository.findByUsername(request.getUsername())
					.orElseThrow(() -> new IllegalArgumentException("User not found"));

			String token = jwtService.generateToken(new UserDetailsImpl(user));

			return new AuthResponse(token);

		} catch (Exception e) {
			System.out.println("username or password is invalid" + e.getMessage());
		}

		return null;
	}

	public DtoUser register(RegisterRequest request) {
		if (userRepository.existsByUsername(request.getUsername())) {
			throw new IllegalArgumentException("This username is already in use.");
		}
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new IllegalArgumentException("This email is already in use.");
		}

		User user = new User();
		System.out.println("name: " + request.getUsername());
		System.out.println("EMAIL: " + request.getEmail());
		System.out.println("password: " + request.getPassword());
		user.setUsername(request.getUsername());
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
