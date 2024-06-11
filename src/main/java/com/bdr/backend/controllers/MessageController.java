package com.bdr.backend.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bdr.backend.models.requests.MessageRequest;
import com.bdr.backend.services.JwtService;
import com.bdr.backend.services.MessageService;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "MessagesController", description = "Routes related to messages")
public class MessageController {

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private JwtService jwtService;

	/**
	 * Send a message to a rental owner
	 * 
	 * @param request	A MessageRequest object containing the message and the rental id
	 * @return ResponseEntity<Map<String, String>> A response containing the status of the message sending
	 */
	@PostMapping("api/messages")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Message send with success", content = @Content(examples = @ExampleObject(value = "{\"message\": \"message send with success\"}"))),
			@ApiResponse(responseCode = "400", description = "Input missing", content = @Content(schema = @Schema())),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())), })

	public ResponseEntity<Map<String, String>> sendMessage(@Valid @RequestBody MessageRequest request) {
		try {
			// Get the userId from the token
			Integer userId =  jwtService.getUserIdFromToken();

			// Create new message in the database
			messageService.createMessage(request.getMessage(), userId, request.getRentalId());

			// Generate response
			Map<String, String> response = new HashMap<>();
			response.put("message", "Message sent with success");
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", "Failed to send message");
			return ResponseEntity.status(400).body(errorResponse);
		}
	}
}
