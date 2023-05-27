package com.example.jwtSecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
//@EnableEurekaClient
public class UserAuthentification {

	public static void main(String[] args) {
		SpringApplication.run(UserAuthentification.class, args);
	}

}