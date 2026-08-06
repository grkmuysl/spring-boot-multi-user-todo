package com.gorkemuysal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gorkemuysal.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
