package com.lzw.newsbean;

public class PrivatePersonChat {
	private String newName;
	private String MessageFromUser;
	private String MessageToUser;
	private String Message;
	
	public PrivatePersonChat(String newName, String messageFromUser, String messageToUser, String message) {
		super();
		this.newName = newName;
		MessageFromUser = messageFromUser;
		MessageToUser = messageToUser;
		Message = message;
	}

	public String getNewName() {
		return newName;
	}
	
	public void setNewName(String newName) {
		this.newName = newName;
	}
	
	public String getMessageFromUser() {
		return MessageFromUser;
	}
	
	public void setMessageFromUser(String messageFromUser) {
		MessageFromUser = messageFromUser;
	}
	
	public String getMessageToUser() {
		return MessageToUser;
	}
	
	public void setMessageToUser(String messageToUser) {
		MessageToUser = messageToUser;
	}
	
	public String getMessage() {
		return Message;
	}
	
	public void setMessage(String message) {
		Message = message;
	}
	
}
