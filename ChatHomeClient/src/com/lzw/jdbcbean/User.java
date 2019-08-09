package com.lzw.jdbcbean;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable{
	private String id;
	private String nickname;
	private String username;
	private String password;
	private Integer state;
	private Date regdate;
	
	public User(){
		
	}
	
	public User(String id, String nickname, String username, String password, Integer state, Date regdate) {
		super();
		this.id = id;
		this.nickname = nickname;
		this.username = username;
		this.password = password;
		this.state = state;
		this.regdate = regdate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getRegdate() {
		return regdate;
	}

	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}
}
