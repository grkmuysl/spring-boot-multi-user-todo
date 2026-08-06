package com.gorkemuysal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gorkemuysal.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUserName(String username);
	
	boolean existsByUsername(String username);
	
	boolean existsByEmail(String email);
}
