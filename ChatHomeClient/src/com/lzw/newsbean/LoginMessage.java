package com.lzw.newsbean;

import java.io.Serializable;
import java.util.List;

import com.lzw.jdbcbean.User;

public class LoginMessage implements Serializable{
     
	private List<User> friendsList;

	public List<User> getFriendsList() {
		return friendsList;
	}

	public void setFriendsList(List<User> friendsList) {
		this.friendsList = friendsList;
	}
	
	
}
