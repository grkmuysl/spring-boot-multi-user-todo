package com.gorkemuysal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.gorkemuysal.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	public static final String AUTHENTICATE = "/auth";
	public static final String REGISTER = "/register";
	
	private final AuthenticationProvider authenticationProvider;
	
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	SecurityConfig(AuthenticationProvider authenticationProvider , JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.authenticationProvider = authenticationProvider;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http)  throws Exception {
		
		  http
          .csrf(csrf -> csrf.disable())
          .authorizeHttpRequests(auth -> auth
              .requestMatchers(AUTHENTICATE , REGISTER).permitAll()
              .anyRequest().authenticated()
          )
          .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
          .authenticationProvider(authenticationProvider)
          .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
          .httpBasic(Customizer.withDefaults());

      return http.build();
		
	}
}
