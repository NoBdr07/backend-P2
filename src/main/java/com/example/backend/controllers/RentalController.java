package com.example.backend.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.models.entities.Rental;
import com.example.backend.services.RentalService;
import com.example.backend.utils.PictureUtils;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "RentalController", description = "Routes related to rentals")
public class RentalController {

	private RentalService rentalService;

	public RentalController(RentalService rentalService) {
		this.rentalService = rentalService;
	}

	private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

	// Get all rentals
	@GetMapping("api/rentals")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Rentals info loaded successfully", content = @Content(examples = @ExampleObject(value = "{\"rentalId\": \"1\", "
					+ "\"name\": \"maison 1\", " + "\"surface\": \"170\", " + "\"price\": \"540000\","
					+ "\"description\": \"example of description\", " + "\"owner_id\": \"1\","
					+ " \"createdAt\": \"2021-10-01T00:00:00Z\", "
					+ "\"updatedAt\": \"2021-10-01T00:00:00Z\"},"), schema = @Schema())),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())), })
	
	public List<Rental> getRentals() {
		List<Rental> rentals = rentalService.getAllRentals();

		if (rentals.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No rentals found");
		}

		return rentals;
	}

	// Get rental by id
	@GetMapping("api/rentals/{rentalId}")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Rental info loaded successfully", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"rentalId\": \"1\", "
					+ "\"name\": \"maison 1\", " + "\"surface\": \"170\", " + "\"price\": \"540000\","
					+ "\"description\": \"example of description\", " + "\"owner_id\": \"1\","
					+ " \"createdAt\": \"2021-10-01T00:00:00Z\", "
					+ "\"updatedAt\": \"2021-10-01T00:00:00Z\"} "), schema = @Schema())),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())), })

	public Rental getRental(@PathVariable int rentalId) {
		Optional<Rental> optionalRental = rentalService.getRentalById(rentalId);

		if (!optionalRental.isPresent()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Rental not found");
		}

		return optionalRental.get();
	}

	// Create a new Rental
	@PostMapping("api/rentals")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Rental created with success", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\": \"Rental created !\"}"))),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())), })
	
	public ResponseEntity<Map<String, String>> createRental(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "surface", required = false) Integer surface,
			@RequestParam(value = "price", required = false) Integer price,
			@RequestParam(value = "picture", required = false) MultipartFile picture,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam("owner_id") Integer owner_id) {
		
		// Manage the file upload
		String filePath = PictureUtils.uploadFile(picture);

		Rental newRental = rentalService.createRental(name, surface, price, filePath, description, owner_id);
		logger.info("Rental created with id: " + newRental.getRentalId());

		Map<String, String> response = new HashMap<>();
		response.put("message", "Rental created !");
		return ResponseEntity.ok(response);
	}

	// Update a rental
	@PutMapping("api/rentals/{rentalId}")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Rental updated !", content = @Content(examples = @ExampleObject(value = "{\"message\": \"Rental updated !\"}"))),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema())), })
	
	public ResponseEntity<Map<String, String>> updateRental(@PathVariable int rentalId,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "surface", required = false) Integer surface,
			@RequestParam(value = "price", required = false) Integer price,
			@RequestParam(value = "picture", required = false) MultipartFile picture,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "owner_id", required = false) Integer owner_id) {
		
		Optional<Rental> optionalRental = rentalService.getRentalById(rentalId);
		
		if (!optionalRental.isPresent()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Rental not found");
		}
		
		String filePath = PictureUtils.uploadFile(picture);
		
		rentalService.updateRental(rentalId, name, surface, price, filePath, description, owner_id);
		
		Map<String, String> response = new HashMap<>();
		response.put("message", "Rental updated !");
		return ResponseEntity.ok(response);
	}
}
