package com.bdr.backend.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bdr.backend.models.entities.User;
import com.bdr.backend.repositories.UserRepository;

@Service
public class DatabaseUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	// This method is use to load the user from the database
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = this.userRepository.findByEmail(username);
		if (user.isEmpty()) {
			throw new UsernameNotFoundException("User not found");
		}
		User appUser = user.get();
	    return org.springframework.security.core.userdetails.User
	            .withUsername(appUser.getEmail())
	            .password(appUser.getPassword())
	            .authorities(new ArrayList<>()) 
	            .build();
	    
	}

}