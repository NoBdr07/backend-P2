package com.bdr.backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
	
	/**
	 * 	Configure Swagger to get an interface that displays all the routes of the API
	 * 
	 * @return OpenAPI object
	 */
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
				.components(new Components().addSecuritySchemes
			            ("Bearer Authentication", createAPIKeyScheme()))
				.info(apiInfo());
	}

	/**
	 * Configure the API info
	 * 
	 * @return Info object
	 */
	private Info apiInfo() {
		return new Info().title("Backend API").description("Backend API for rentals app").version("1.0");
	}
	

	/**
	 * Configure the API key scheme for the Swagger interface
	 * 
	 * @return SecurityScheme object
	 */
	private SecurityScheme createAPIKeyScheme() {
	    return new SecurityScheme().type(SecurityScheme.Type.HTTP)
	        .bearerFormat("JWT")
	        .scheme("bearer");
	}


}