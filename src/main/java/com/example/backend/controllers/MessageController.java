package com.example.backend.controllers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.models.entities.Message;
import com.example.backend.models.requests.MessageRequest;
import com.example.backend.services.MessageService;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "MessagesController", description = "Routes related to messages")
public class MessageController {

	private MessageService messageService;

	public MessageController(MessageService messageService) {
		this.messageService = messageService;
	}

	private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Message send with success", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\": \"message send with success\"}"))),
			@ApiResponse(responseCode = "400", description = "Input missing", content = @Content(schema = @Schema())), })

	@PostMapping("api/messages/")
	public ResponseEntity<Map<String, String>> sendMessage(@Valid @RequestBody MessageRequest request) {
		try {
			logger.debug("Received message request: {}", request.getMessage()); // for debug
			// Validate request fields
			if (request.getUserId() == null || request.getRentalId() == null || request.getMessage() == null) {
				throw new IllegalArgumentException("Missing required fields");
			}
			// Create new message in the database
			Message message = messageService.createMessage(request.getMessage(), request.getUserId(), request.getRentalId());
			messageService.save(message);

			// Generate response
			Map<String, String> response = new HashMap<>();
			response.put("message", "Message sent with success");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("Error creating message", e);
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", "Failed to send message");
			return ResponseEntity.status(400).body(errorResponse);
		}
	}
}
