package com.bdr.backend.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.bdr.backend.models.dtos.UserDto;
import com.bdr.backend.models.entities.User;
import com.bdr.backend.services.UserService;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "UserController", description = "Routes related to users")
public class UserController {
	
	@Autowired
	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	// Get user info by userId
	@GetMapping("api/user/{userId}")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User info loaded successfully", 
					content = @Content(examples = @ExampleObject(
							value = "{\"userId\": \"1\", \"email\": \"test@test.com\", \"name\": \"test\", "
									+ "\"createdAt\": \"2021/10/01\", \"updatedAt\": \"2021/10/01\"}"), 
					schema = @Schema(implementation = UserDto.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())), })

	public UserDto getUser(@PathVariable int userId) {

		Optional<User> optionalUser = userService.getUserFromUserId(userId);

		if (!optionalUser.isPresent()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found");
		}

		User user = optionalUser.get();
		
		return userService.convertToDto(user);

	}
}
