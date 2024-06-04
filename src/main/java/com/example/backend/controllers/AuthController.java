package com.example.backend.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.models.RegisterRequest;
import com.example.backend.models.User;
import com.example.backend.services.JwtService;
import com.example.backend.services.UserService;

@RestController
public class AuthController {

	private JwtService jwtService;
	private UserService userService;
	private BCryptPasswordEncoder passwordEncoder;

	public AuthController(JwtService jwtService, UserService userService, BCryptPasswordEncoder passwordEncoder) {
		this.jwtService = jwtService;
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping("api/auth/register")
	public Map<String, String> register(@Valid @RequestBody RegisterRequest request) {
		// Create new user in the db
		User user = new User();
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setName(request.getName());
		
		userService.saveUser(user);
		
		String token = jwtService.generateToken(request.getEmail());
		Map<String, String> tokenResponse = new HashMap<>();
		tokenResponse.put("token", token);
		return tokenResponse;
	}

	@GetMapping("api/rentals")
	public String getRentals() {
		return "List of rentals";
	}

}
