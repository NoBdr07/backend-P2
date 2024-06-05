package com.example.backend.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class MessageRequest {
	@Schema(description = "Message content", name = "message")
	private String message;

	@Schema(description = "User_id of the author", name = "user_id")
	@JsonProperty("user_id")
	private Integer userId;

	@Schema(description = "Rental concerned by the message", name = "rental_id")
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
