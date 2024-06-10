# Back-end du projet 3 : Développez le back-end en utilisant Java et Spring

This projet is a REST API developped on Eclipse with Spring and specifically Spring Security.    
It was made to handle data requests for the front-end rental app ChâTop.   
It allows users of this app to :  
	- Register  
	- Login  
	- Get all rentals data  
	- See specific data concerning a rental  
	- Create a new rental  
	- Send a message to a rental owner  
	- See infos about its own account.  

## Installation

Clone this projet from the repository github : https://github.com/NoBdr07/backend-P3

Create your database with the following commands : 

	CREATE DATABASE rentalapp;

	CREATE TABLE users (
		id int NOT NULL AUTO_INCREMENT,
		email varchar(255) DEFAULT NULL,
		name varchar(255) DEFAULT NULL,
		password varchar(255) DEFAULT NULL,
		created_at timestamp NULL DEFAULT NULL,
		updated_at timestamp NULL DEFAULT NULL,
		PRIMARY KEY (id),
		UNIQUE KEY USERS_index (email) );

	CREATE TABLE rentals (
		id int NOT NULL AUTO_INCREMENT,
		name varchar(255) DEFAULT NULL,
		surface decimal(10,0) DEFAULT NULL,
		price decimal(10,0) DEFAULT NULL,
		picture varchar(255) DEFAULT NULL,
		description varchar(2000) DEFAULT NULL,
		owner_id int NOT NULL,
		created_at timestamp NULL DEFAULT NULL,
		updated_at timestamp NULL DEFAULT NULL,
		PRIMARY KEY (id),
		KEY idx_owner_id (owner_id),
		CONSTRAINT fk_owner_id FOREIGN KEY (owner_id) REFERENCES users (id) );

	CREATE TABLE messages (
		id int NOT NULL AUTO_INCREMENT,
		rental_id int DEFAULT NULL,
		user_id int DEFAULT NULL,
		message varchar(2000) DEFAULT NULL,
		created_at timestamp NULL DEFAULT NULL,
		updated_at timestamp NULL DEFAULT NULL,
		PRIMARY KEY (id),
		KEY idx_user_id (user_id),
		KEY idx_rental_id (rental_id),
		CONSTRAINT fk_rental_id FOREIGN KEY (rental_id) REFERENCES rentals (id),
		CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users (id) );
		
Then you'll have to add environment variable for the sensitive data :   
	- SPRING_DATASOURCE_USERNAME : for your mysql username,  
	- SPRING_DATASOURCE_PASSWORD : for your mysql password,  
	- JWT_SECRET : for the secret key of the jwt tokens, it must be at least 16 characters.  
	
In application.properties, you can change the port if you're not going to use 3001.

## Architecture 
```
+---src
|   +---main
|   |   +---java
|   |   |   \---com
|   |   |       \---bdr
|   |   |           \---backend
|   |   |               |   BackendApplication.java
|   |   |               |
|   |   |               +---configuration
|   |   |               |       JwtTokenUtil.java		(generate token for authentication)
|   |   |               |       PasswordEncoderConfig.java		
|   |   |               |       SpringSecurityConfig.java		
|   |   |               |       SwaggerConfig.java	(configure swagger to have documented routes)
|   |   |               |       WebConfig.java	(configure picture file management)
|   |   |               |
|   |   |               +---controllers
|   |   |               |       AuthController.java
|   |   |               |       MessageController.java
|   |   |               |       RentalController.java
|   |   |               |       UserController.java
|   |   |               |
|   |   |               +---models
|   |   |               |   +---dtos
|   |   |               |   |       MessageDto.java
|   |   |               |   |       RentalDto.java
|   |   |               |   |       UserDto.java
|   |   |               |   |
|   |   |               |   +---entities
|   |   |               |   |       Message.java
|   |   |               |   |       Rental.java
|   |   |               |   |       User.java
|   |   |               |   |
|   |   |               |   \---requests
|   |   |               |           LoginRequest.java
|   |   |               |           MessageRequest.java
|   |   |               |           RegisterRequest.java
|   |   |               |           TokenResponse.java
|   |   |               |
|   |   |               +---repositories
|   |   |               |       MessageRepository.java
|   |   |               |       RentalRepository.java
|   |   |               |       UserRepository.java
|   |   |               |
|   |   |               +---services
|   |   |               |       DatabaseUserDetailsService.java
|   |   |               |       MessageService.java
|   |   |               |       RentalService.java
|   |   |               |       UserService.java
|   |   |               |
|   |   |               \---utils
|   |   |                       DateUtils.java		(manage date format compatibility with mysql)
|   |   |                       PictureUtils.java		(manage picture storage in /uploads and db)
|   |   |
|   |   \---resources
|   |       |   application.properties 
|   |       |
|   |       +---static
|   |       |   \---uploads	(rentals pictures storage)
|   |       |
|   |       \---templates  
```
## Swagger

Swagger is configured in this API to display clear information about the routes.   
You can go to this url to see an interactive interface : http://localhost:3001/swagger-ui/index.html   
Or to this url to get a json files about the routes : http://localhost:3001/v3/api-docs  