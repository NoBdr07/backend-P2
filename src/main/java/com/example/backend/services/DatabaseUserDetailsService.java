package com.example.backend.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.backend.models.User;
import com.example.backend.repositories.UserRepository;

@Service
public class DatabaseUserDetailsService implements UserDetailsService{
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	public DatabaseUserDetailsService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
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
