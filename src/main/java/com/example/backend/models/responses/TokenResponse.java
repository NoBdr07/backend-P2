package com.example.backend.models.responses;

import io.swagger.v3.oas.annotations.media.Schema;

public class TokenResponse {
	@Schema(description = "Token", name = "token")
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
