package com.chat_application.chat_application.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity 
public class Message {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String sender; 
	private String recipient; 
	private String content; 
	private ChatMessage.MessageType type;

	public Message() {}

	public Message(Long id, String sender, String recipient, String content, ChatMessage.MessageType type) {
		this.id = id; 
		this.sender = sender; 
		this.recipient = recipient; 
		this.content = content; 
		this.type = type; 
	}

	// Getters and Setters

	public Long getId() { return id; }

	public void setId(Long id) { this.id = id; }

	public String getSender() { return sender; }

	public void setSender(String sender) { this.sender = sender; }

	public String getRecipient() { return recipient; }

	public void setRecipient(String recipient) { this.recipient = recipient; }

	public String getContent() { return content; }

	public void setContent(String content) { this.content = content; }

	public ChatMessage.MessageType getType() { return type; }

	public void setType(ChatMessage.MessageType type) { this.type = type; }
}