package com.lzw.client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import com.lzw.handleNews.HandlViewPChat;
import com.lzw.handleNews.HandleServer;
import com.lzw.jdbcbean.User;
import com.lzw.newsbean.LoginMessage;
import com.lzw.newsbean.ViewPChat;
import com.lzw.prothread.ProPCView;
import com.lzw.window.LoginWindow;
import com.lzw.window.MainWindow;
import com.lzw.window.PCView;


public class ChatClinet{
	public static void main(String[]args){
		
		//网络访问信息
		String serverAddress = "127.0.0.1";
		Integer port = 5000;
		PrintStream printStream = null;
		Scanner scannerServer = null;
		String serverMsg = null;
		
		//用于获取客户端线程
		List<Socket> getClient = new ArrayList<>();
		Object loginWinLock = new Object();
		LoginWindow loginWin = new LoginWindow(serverAddress,port,getClient,loginWinLock);
		
		//等待登录完成   登录完成后登录界面关闭  唤醒主线程
		synchronized (loginWinLock) {
			try {
				loginWinLock.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//读取好友列表集合，创建好友界面
		Socket client = getClient.get(0);
		getClient.remove(0);
		ObjectInputStream oi = null;
		Object obj = null;
		try {
			oi = new ObjectInputStream(client.getInputStream());
			obj = oi.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//获取好友列表
		LoginMessage loginMsg = (LoginMessage)obj;
		List<User> friendslist = loginMsg.getFriendsList();
		System.out.println("一共传递了:"+friendslist.size());
		
		//创建私聊窗口的容器
		ProPCView proPCView = new ProPCView(new HashMap<String,PCView>());
		
		//创建界面主窗口
		MainWindow mainwindow = new MainWindow(friendslist,proPCView,client);		
		
		//创建容器集合
		Map<String,List> mapList = new HashMap<String,List>();
		//创建容器锁集合
		Map<String,Object> mapLock = new HashMap<String,Object>();
		
		//启动接收私聊消息的线程并创建消息队列
		List<ViewPChat> listViewPChat = new ArrayList<>(); 
		Object viewPCLock = new Object();
		mapList.put("listViewPChat",listViewPChat);
		mapLock.put("viewPCLock",viewPCLock);
		Thread viewPChat = new Thread(new HandlViewPChat(listViewPChat,viewPCLock,proPCView));
		viewPChat.start();
		
		//启动处理服务端返回的线程
		Thread handleServer = new Thread(new HandleServer(client,mapList,mapLock));
		handleServer.start();
		
		while(true){
			
		}
	}
}
