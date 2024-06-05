package com.example.backend.controllers;

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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.models.entities.User;
import com.example.backend.models.requests.LoginRequest;
import com.example.backend.models.requests.RegisterRequest;
import com.example.backend.models.responses.UserResponse;
import com.example.backend.services.JwtService;
import com.example.backend.services.UserService;
import com.nimbusds.oauth2.sdk.TokenResponse;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "AuthController", description = "REST APIs related to Auth")
public class AuthController {

	private JwtService jwtService;
	private UserService userService;
	private BCryptPasswordEncoder passwordEncoder;

	public AuthController(JwtService jwtService, UserService userService, BCryptPasswordEncoder passwordEncoder) {
		this.jwtService = jwtService;
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}

	// Register a new user
	@PostMapping("api/auth/register")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User registered successfully", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"token\": \"jwt\"}"), schema = @Schema(implementation = TokenResponse.class))),
			@ApiResponse(responseCode = "400", description = "Input missing", content = @Content(schema = @Schema())), })
	public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequest request) {
		// Create new user in the database
		User user = new User();
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setName(request.getName());

		userService.saveUser(user);

		// Generate token
		String token = jwtService.generateToken(request.getEmail());
		Map<String, String> tokenResponse = new HashMap<>();
		tokenResponse.put("token", token);

		return new ResponseEntity<>(tokenResponse, HttpStatus.OK);

	}

	// Handle input missing errors
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	// Login with a known user
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User logged in successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema())),
			})
	@PostMapping("api/auth/login")
	public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
		User user = userService.getUserByEmail(request.getLogin()).get();
		
		if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		String token = jwtService.generateToken(user.getEmail());
		Map<String, String> tokenResponse = new HashMap<>();
		tokenResponse.put("token", token);

		return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
	}
	
	// Get the user's info
	@GetMapping("api/auth/me")
	public UserResponse getCurrentUser() {
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

			UserResponse userResponse = userService.setUserResponse(user.getUserId(), user.getEmail(), user.getName(), user.getCreatedAt(), user.getUpdatedAt());

	        return userResponse;
		}
		throw new IllegalStateException("User is not authenticated");
	}

}
