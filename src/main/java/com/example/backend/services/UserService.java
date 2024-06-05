package com.example.backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.models.User;
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
	
	

		 
}
