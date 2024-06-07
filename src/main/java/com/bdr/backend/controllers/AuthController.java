package com.bdr.backend.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.bdr.backend.configuration.JwtTokenUtil;
import com.bdr.backend.models.dtos.UserDto;
import com.bdr.backend.models.entities.User;
import com.bdr.backend.models.requests.LoginRequest;
import com.bdr.backend.models.requests.RegisterRequest;
import com.bdr.backend.services.UserService;
import com.nimbusds.oauth2.sdk.TokenResponse;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "AuthController", description = "Routes related to authentication")
public class AuthController {

	private JwtTokenUtil jwtService;
	private UserService userService;
	private BCryptPasswordEncoder passwordEncoder;

	public AuthController(JwtTokenUtil jwtService, UserService userService, BCryptPasswordEncoder passwordEncoder) {
		this.jwtService = jwtService;
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}

	// Register a new user
	@PostMapping("api/auth/register")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User registered successfully", content = @Content(examples = @ExampleObject(value = "{\"token\": \"jwt\"}"), schema = @Schema(implementation = TokenResponse.class))),
			@ApiResponse(responseCode = "400", description = "Input missing", content = @Content(schema = @Schema())), })
	
	public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequest request) {
		
		// Create new user in the database	
		userService.createUser(request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getName());

		// Generate token
		String token = jwtService.generateToken(request.getEmail());
		Map<String, String> tokenResponse = new HashMap<>();
		tokenResponse.put("token", token);

		return new ResponseEntity<>(tokenResponse, HttpStatus.OK);

	}

	// Login with a known user
	@PostMapping("api/auth/login")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User logged in successfully", content = @Content(examples = @ExampleObject(value = "{\"token\": \"jwt\"}"), schema = @Schema(implementation = TokenResponse.class))),
			@ApiResponse(responseCode = "401", description = "Invalid input", content = @Content(examples = @ExampleObject(value = "{\"message\": \"error\"}"), schema = @Schema())), })	
	
	public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
		User user = userService.getUserByEmail(request.getEmail()).get();

		if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			Map<String, String> errorResponse = new HashMap<>();
	        errorResponse.put("message", "error");
			return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
		}

		String token = jwtService.generateToken(user.getEmail());
		Map<String, String> tokenResponse = new HashMap<>();
		tokenResponse.put("token", token);

		return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
	}

	// Get the user's info
	@GetMapping("api/auth/me")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User info loaded successfully", content = @Content(mediaType = "application/json", 
					examples = @ExampleObject(value = "{\"userId\": \"1\", \"email\": \"test@test.com\", \"name\": \"test\", \"createdAt\": \"2021-10-01T00:00:00Z\", \"updatedAt\": \"2021-10-01T00:00:00Z\"}"), 
					schema = @Schema(implementation = UserDto.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())), })
	
	public UserDto getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
			Jwt jwt = (Jwt) authentication.getPrincipal();
			String login = jwt.getClaim("login");

			if (login == null) {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login claim not found in token");
			}

			Optional<User> optionalUser = userService.getUserByEmail(login);

			if (!optionalUser.isPresent()) {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
			}

			User user = optionalUser.get();

			return userService.convertToDto(user);
		}
		throw new IllegalStateException("User is not authenticated");
	}


}
