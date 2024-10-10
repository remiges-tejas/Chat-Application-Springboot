package com.chat_application.chat_application.controller;

import com.chat_application.chat_application.model.ChatMessage;
import com.chat_application.chat_application.model.Message;
import com.chat_application.chat_application.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:5174") 
@RestController
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public ChatController(SimpMessagingTemplate simpMessagingTemplate, ChatService chatService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatService = chatService;
    }

    @MessageMapping("/publicMessage")
    @SendTo("/topic/public")
    public ChatMessage sendPublicMessage(ChatMessage message) {
        chatService.saveMessage(new Message(null, message.getSender(), null, message.getContent(), message.getType()));
        return message;
    }

    @MessageMapping("/privateMessage")
    public void sendPrivateMessage(ChatMessage message) {
        chatService.saveMessage(new Message(null, message.getSender(), message.getRecipient(), message.getContent(), message.getType()));
        simpMessagingTemplate.convertAndSendToUser(message.getRecipient(), "/queue/private", message);
    }
}