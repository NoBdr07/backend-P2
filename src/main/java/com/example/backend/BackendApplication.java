package com.example.backend;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.backend.models.User;
import com.example.backend.services.UserService;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args){
		SpringApplication.run(BackendApplication.class, args);
	}
	
	
	

}
