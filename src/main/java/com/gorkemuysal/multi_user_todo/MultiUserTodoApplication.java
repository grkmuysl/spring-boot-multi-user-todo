package com.gorkemuysal.multi_user_todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages =  {"com.gorkemuysal"})
@EnableJpaRepositories(basePackages = {"com.gorkemuysal"})
@ComponentScan(basePackages = {"com.gorkemuysal"})
@SpringBootApplication
public class MultiUserTodoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultiUserTodoApplication.class, args);
	}

}
