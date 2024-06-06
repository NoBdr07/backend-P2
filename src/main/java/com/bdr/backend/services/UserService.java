package com.bdr.backend.services;

import java.util.Date;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bdr.backend.models.dtos.UserDto;
import com.bdr.backend.models.entities.User;
import com.bdr.backend.repositories.UserRepository;
import com.bdr.backend.utils.DateUtils;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

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

	public User createUser(String email, String password, String name) {
		User newUser = new User();
		newUser.setEmail(email);
		newUser.setPassword(password);
		newUser.setName(name);

		String formattedDate = DateUtils.formatToMySQLDateTime(new Date());
		newUser.setCreatedAt(formattedDate);

		return userRepository.save(newUser);
	}

	public UserDto convertToDto(User user) {
		return modelMapper.map(user, UserDto.class);
	}

}
