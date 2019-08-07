package com.lzw.newsbean;

import java.io.Serializable;
import java.net.Socket;
import java.util.Map;

public class LoginNew implements Serializable{
	private String newName;
	private String userName;
	private String password;
	private Socket socket;
	private Map<String,Socket> mapSocket;
	
	public LoginNew(String newName, String userName, String password,Socket socket,Map<String,Socket> mapSocket) {
		super();
		this.newName = newName;
		this.userName = userName;
		this.password = password;
		this.socket = socket;
		this.mapSocket = mapSocket;
	}
	
	public Map<String, Socket> getMapSocket() {
		return mapSocket;
	}

	public void setMapSocket(Map<String, Socket> mapSocket) {
		this.mapSocket = mapSocket;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public LoginNew(){
		
	}
	
	public String getNewName() {
		return newName;
	}
	
	public void setNewName(String newName) {
		this.newName = newName;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
}
