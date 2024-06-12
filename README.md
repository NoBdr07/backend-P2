# Back-end du projet 3 : Développez le back-end en utilisant Java et Spring

This projet is a REST API developped on Eclipse with Spring 3.3.0 and Java 17.    
It was made to handle data requests for the front-end rental app ChâTop.   
It allows users of this app to :  
<ul>
	<li>Register  </li>
	<li>Login  </li>
	<li>Get all rentals data  </li>
	<li>See specific data concerning a rental  </li>
	<li>Create a new rental  </li>
	<li>Send a message to a rental owner  </li>
	<li>See infos about its own account.  </li>
	
</ul>

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

If you use a different name for the database, change the name of the database in application.properties.  
		
Then you'll have to add environment variable for the sensitive data :<ul>
	<li>SPRING_DATASOURCE_USERNAME : for your mysql username,  </li>
	<li>SPRING_DATASOURCE_PASSWORD : for your mysql password, </li>
	<li>JWT_SECRET : for the secret key of the jwt tokens, it must be at least 16 characters. </li>  
</ul>

In application.properties, you can change the port if you're not going to use 3001.

Then you can do a    `mvn spring-boot:run`     to run the API.

You can try it with postman or directly from the front-end.  
Make sure that the front-end is set to use an API on server port 3001 or the one you chose.

Before trying any routes, check if the database have the data needed.  
For example, if you want to try the POST/message, you need to have in database a corresponding user and a corresponding rental.  
You can add data with the help of Postman, start by registering some users and then some rentals.  
Then you'll be able to try every routes.
For the routes that need authentication, you need to set the authorization in postman.
Select the Auth Type "Bearer Token" and put the token you got from the login.


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
|   |   |               |
|   |   |               +---repositories
|   |   |               |       MessageRepository.java
|   |   |               |       RentalRepository.java
|   |   |               |       UserRepository.java
|   |   |               |
|   |   |               +---services
|   |   |               |       JwtService.java 		(generate token for authentication)
|   |   |               |       MessageService.java
|   |   |               |       RentalService.java
|   |   |               |       UserService.java
|   |   |               |
|   |   |               +---servicesImpl
|   |   |               |       MessageServiceImpl.java
|   |   |               |       RentalServiceImpl.java
|   |   |               |       UserServiceImpl.java
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

## Security

Requests security is managed with jwt tokens :  
--> Users login with their email and password --> the API check if they are present in the database --> give a token to access all the routes.

The authentication logic is managed by AuthController (check if the name and password are right) and JwtService (generate the token).

## Data

Pictures that illustrate the rentals are stored in src/main/resource/static/uploads.   
Their relative url (example : /uploads/picture_name.jpg) are stored in the table Rentals of the database.  
This solution is working well for a small app. If the app grows, it would be a better practice to use a cloud.