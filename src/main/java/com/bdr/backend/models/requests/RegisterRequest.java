package com.bdr.backend.models.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public class RegisterRequest {
	@Schema(description = "User email", name = "email", example = "testBis@test.com")
	@NotBlank(message = "Email cannot be null")
	@Pattern(regexp = ".+@.+\\..+", message = "Email should be valid")
	private String email;

	@Schema(description = "User name", name = "name", example = "TEST2 test2")
	@NotBlank(message = "Name cannot be null")
	@Size(min = 4, message = "Name should have at least 4 characters")
	private String name;

	@Schema(description = "User password", name = "password", example = "testBis345!")
	@NotBlank(message = "Name cannot be null")
	@Size(min = 6, message = "Password should have at least 6 characters")
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

	public void setName( String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
