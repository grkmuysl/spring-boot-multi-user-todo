package com.gorkemuysal.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.gorkemuysal.entity.UserDetailsImpl;
import com.gorkemuysal.repository.UserRepository;

@Configuration
public class AppConfig {

	private static SecurityConfig securityConfig;

	private final UserRepository userRepository;

	AppConfig(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				Optional<UserDetailsImpl> optional = userRepository.findByUsername(username);

				if (optional.isPresent()) {
					return optional.get();
				}

				return null;

			}
		};
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService());
		authenticationProvider.setPasswordEncoder(securityConfig.passwordEncoder());

		return authenticationProvider;
	}

}
