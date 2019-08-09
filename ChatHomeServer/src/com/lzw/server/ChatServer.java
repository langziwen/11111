package com.lzw.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.lzw.handleNews.HandleLogin;
import com.lzw.handleNews.HandlePPChat;
import com.lzw.newsbean.*;

public class ChatServer {
	
	public static void main(String[]args){
		
		
		//存取用户信息和socket对象
		Map<String,Socket> mapSocket = new HashMap<String,Socket>();
		
		//登录消息队列
		List<LoginNew> listLoginNews = new ArrayList<LoginNew>();
		
		//私聊消息队列
		List<PrivatePersonChat> ppcList = new ArrayList<PrivatePersonChat>();
		
		//消息队列集合
		Map<String,List> mapList = new HashMap<String,List>();
		mapList.put("PrivatePersonChat", ppcList);
		
		//线程锁集合
		Map<String,Object> lockList = new HashMap<String,Object>();
		
		//创建私聊线程
		Object ppChatLock = new Object();
		lockList.put("ppChatLock",ppChatLock);
		Thread handlePPChat = new Thread(new HandlePPChat(mapSocket,ppcList,ppChatLock));
		handlePPChat.start();
		
		//创建处理消息线程
		Object loginLock = new Object();
		lockList.put("loginLock",loginLock);
		Thread handleLoginThread = new Thread(new HandleLogin(listLoginNews,loginLock,mapList,lockList));
		handleLoginThread.start();
		
		ServerSocket serversocket = null;
		try {
		   serversocket = new ServerSocket(5000);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//从客户端读取登录对象
		ObjectInputStream oi= null;
		ObjectOutputStream os = null;
		Socket socket = null;
		
		while(true){
			try {
				socket = serversocket.accept();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			try {
				oi = new ObjectInputStream(socket.getInputStream());
				os = new ObjectOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Object ob = null;
			try {
				ob = oi.readObject();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			System.out.println(ob.getClass().getName());
			LoginNew tempLogin = (LoginNew)ob;
			tempLogin.setSocket(socket);
			tempLogin.setMapSocket(mapSocket);
			if(tempLogin != null){
				
				synchronized (loginLock){
					listLoginNews.add(tempLogin);
					loginLock.notify();
				}
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
}




