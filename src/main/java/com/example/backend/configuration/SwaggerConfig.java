package com.example.backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
	 @Bean
	    public OpenAPI customOpenAPI() {
	        return new OpenAPI().info(apiInfo());
	    }

	    private Info apiInfo() {
	        return new Info()
	                .title("Backend API")
	                .description("Backend API for rentals app")
	                .version("1.0");
	    }

	}