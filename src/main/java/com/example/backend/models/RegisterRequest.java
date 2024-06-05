package com.example.backend.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class RegisterRequest {
	@Schema(description = "User email", name = "email")
	@NotNull(message = "Email cannot be null")
	private String email;
	
	@Schema(description = "User name", name = "name")
	@NotNull(message = "Name cannot be null")
	private String name;
	
	@Schema(description = "User password", name = "password")
	@NotNull(message = "Name cannot be null")
	private String password;
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
