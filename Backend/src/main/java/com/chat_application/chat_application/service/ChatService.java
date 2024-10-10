package com.chat_application.chat_application.service;

import com.chat_application.chat_application.model.Message; 
import com.chat_application.chat_application.repository.MessageRepository; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service;

import java.util.List;

@Service 
public class ChatService {

	@Autowired 
	private MessageRepository messageRepository;

	public List<Message> getMessages() {
		return messageRepository.findAll(); 
	}

	public Message saveMessage(Message message) {
		return messageRepository.save(message); 
	}
}