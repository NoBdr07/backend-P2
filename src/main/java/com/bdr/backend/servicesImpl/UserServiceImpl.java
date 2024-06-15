package com.bdr.backend.servicesImpl;

import java.util.Date;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bdr.backend.models.dtos.UserDto;
import com.bdr.backend.models.entities.User;
import com.bdr.backend.repositories.UserRepository;
import com.bdr.backend.services.UserService;
import com.bdr.backend.utils.DateUtils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Get user from user id
	 * 
	 * @param userId Id of the user
	 * @return Optional<User>
	 */
	public Optional<User> getUserFromUserId(int userId) {
		return userRepository.findById(userId);
	}

	/**
	 * Save user
	 * 
	 * @param user User to save
	 * @return User
	 */
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	/**
	 * Get user from email
	 * 
	 * @param email Email of the user
	 * @return Optional<User>
	 */
	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	/**
	 * Create user
	 * 
	 * @param email email of user
	 * @param password password encoded
	 * @param name name of user
	 * @return User
	 */
	public User createUser(String email, String password, String name) {
		User newUser = new User();
		newUser.setEmail(email);
		newUser.setPassword(password);
		newUser.setName(name);

		String formattedDate = DateUtils.formatToMySQLDateTime(new Date());
		newUser.setCreatedAt(formattedDate);

		return userRepository.save(newUser);
	}

	/**
	 * Convert UserDto to User
	 * 
	 * @param user user to convert
	 * @return UserDto
	 */
	public UserDto convertToDto(User user) {
		return modelMapper.map(user, UserDto.class);
	}

}
