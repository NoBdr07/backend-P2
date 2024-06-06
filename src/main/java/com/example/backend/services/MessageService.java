package com.example.backend.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.models.entities.Message;
import com.example.backend.repositories.MessageRepository;
import com.example.backend.utils.DateUtils;

@Service
public class MessageService {
	
	@Autowired
	private MessageRepository messageRepository;
	
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

}
