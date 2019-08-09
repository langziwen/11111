package com.lzw.newsbean;

import java.io.Serializable;

public class PrivatePersonChat implements Serializable{
	private String MessageFromUser;
	private String MessageToUser;
	private String Message;
	private Integer num;
	
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public PrivatePersonChat(){
		
	}
	
	public PrivatePersonChat(String messageFromUser, String messageToUser, String message) {
		super();
		MessageFromUser = messageFromUser;
		MessageToUser = messageToUser;
		Message = message;
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
