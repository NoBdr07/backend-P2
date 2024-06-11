package com.bdr.backend.servicesImpl;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bdr.backend.models.dtos.MessageDto;
import com.bdr.backend.models.entities.Message;
import com.bdr.backend.repositories.MessageRepository;
import com.bdr.backend.services.MessageService;
import com.bdr.backend.utils.DateUtils;

@Service
public class MessageServiceImpl implements MessageService {
	
	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Save a message
	 * 
	 * @param message The message to save, it contains the message, the user id and the rental id.
	 */
	public void save(Message message) {
		messageRepository.save(message);
	}

	/**
	 * Create a message
	 * 
	 * @param message  The content of the message
	 * @param userId   The user id of the user that send the message
	 * @param rentalId The rental id of the rental that the message is related to
	 * @return The message created
	 */
	public Message createMessage(String message, Integer userId, Integer rentalId) {
		Message newMessage = new Message();
		newMessage.setMessage(message);
		newMessage.setUserId(userId);
		newMessage.setRentalId(rentalId);
		newMessage.setCreatedAt(DateUtils.formatToMySQLDateTime(new Date()));

		messageRepository.save(newMessage);

		return newMessage;
	}
	
	/**
	 * Convert a message to a message dto
	 * 
	 * @param message The message to convert
	 * @return The message dto
	 */
	public MessageDto convertToDto(Message message) {
		return modelMapper.map(message, MessageDto.class);
	}

}
