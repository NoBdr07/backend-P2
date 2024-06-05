package com.example.backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.models.entities.User;
import com.example.backend.models.responses.UserResponse;
import com.example.backend.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public Optional<User> getUserFromUserId(int userId) {
        return userRepository.findById(userId);
    }
	
	public User saveUser(User user) {
        // Make sure to hash the password before saving
        return userRepository.save(user);
    }
	
	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public UserResponse setUserResponse(Integer UserId, String email, String name, String createdAt, String updatedAt) {
		UserResponse userResponse = new UserResponse();
		userResponse.setUserId(UserId);
		userResponse.setEmail(email);
		userResponse.setName(name);
		userResponse.setCreatedAt(createdAt);
		userResponse.setUpdatedAt(updatedAt);
		return userResponse;
	}
	
	

		 
}
