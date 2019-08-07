package com.lzw.newsbean;

public class ViewPChat {
	private String fromUser;
	private String ToUser;
	private String Message;
	
	public ViewPChat(String fromUser, String toUser, String message) {
		super();
		this.fromUser = fromUser;
		ToUser = toUser;
		Message = message;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getToUser() {
		return ToUser;
	}

	public void setToUser(String toUser) {
		ToUser = toUser;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}
	
	
}
