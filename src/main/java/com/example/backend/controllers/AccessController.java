package com.example.backend.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.services.JwtService;

@RestController
public class AccessController {
	
	@Autowired
	private JwtService jwtService;
	
	public AccessController(JwtService jwtService) {
		this.jwtService = jwtService;
    }
	
	@PostMapping("api/auth/login")
	public Map<String, String> getToken(Authentication authentication) {
	    String token = jwtService.generateToken(authentication);
	    Map<String, String> tokenResponse = new HashMap<>();
	    tokenResponse.put("token", token);
	    return tokenResponse;
	}
	
	@GetMapping("api/rentals")
	public String getRentals() {
		return "List of rentals";
	}

}
