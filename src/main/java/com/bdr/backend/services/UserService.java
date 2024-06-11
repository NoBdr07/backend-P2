package com.bdr.backend.services;

import java.util.Optional;

import com.bdr.backend.models.entities.User;

public interface UserService {
	
	Optional<User> getUserFromUserId(int userId);
	
	User saveUser(User user);
	
	Optional<User> getUserByEmail(String email);
	
	User createUser(String email, String password, String name);

}
