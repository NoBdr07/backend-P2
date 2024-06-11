package com.bdr.backend.services;

import com.bdr.backend.models.dtos.MessageDto;
import com.bdr.backend.models.entities.Message;

public interface MessageService {
	
	Message createMessage(String message, Integer userId, Integer rentalId);
	
	MessageDto convertToDto(Message message);

}
