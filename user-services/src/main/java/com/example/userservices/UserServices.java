package com.example.userservices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan({"com.example.jwtSecurity.services","com.example.userservices",})
//@EnableMongoRepositories("com.example.jwtSecurity")
//@ComponentScan(basePackages = "com.example.jwtSecurity")
public class UserServices {

	public static void main(String[] args) {
		SpringApplication.run(UserServices.class, args);
	}

}
