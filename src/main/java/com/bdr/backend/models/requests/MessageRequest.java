package com.bdr.backend.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MessageRequest {
	@Schema(description = "Message content", name = "message", example = "Hello, I have a question about the rental")
	@NotBlank(message = "Message cannot be empty")
	private String message;

	@Schema(description = "User_id of the author", name = "user_id", example = "18")
	@JsonProperty("user_id")
	private Integer userId;

	@Schema(description = "Rental concerned by the message", name = "rental_id", example = "4")
	@NotNull(message = "Rental_id cannot be empty")
	@JsonProperty("rental_id")
	private Integer rentalId;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer newId) {
		this.userId = newId;
	}

	public Integer getRentalId() {
		return rentalId;
	}

	public void setRentalId(Integer newId) {
		this.rentalId = newId;
	}
}
