package com.bdr.backend.services;

import java.util.Date;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bdr.backend.models.dtos.MessageDto;
import com.bdr.backend.models.entities.Message;
import com.bdr.backend.repositories.MessageRepository;
import com.bdr.backend.utils.DateUtils;

@Service
public class MessageService {
	
	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	public void save(Message message) {
		messageRepository.save(message);
	}

	public Message createMessage(String message, Integer userId, Integer rentalId) {
		Message newMessage = new Message();
		newMessage.setMessage(message);
		newMessage.setUserId(userId);
		newMessage.setRentalId(rentalId);
		newMessage.setCreatedAt(DateUtils.formatToMySQLDateTime(new Date()));

		messageRepository.save(newMessage);

		return newMessage;
	}
	
	public MessageDto convertToDto(Message message) {
		return modelMapper.map(message, MessageDto.class);
	}

}
