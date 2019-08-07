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
		
		//���������Ϣ
		String serverAddress = "127.0.0.1";
		Integer port = 5000;
		PrintStream printStream = null;
		Scanner scannerServer = null;
		String serverMsg = null;
		
		//���ڻ�ȡ�ͻ����߳�
		List<Socket> getClient = new ArrayList<>();
		Object loginWinLock = new Object();
		LoginWindow loginWin = new LoginWindow(serverAddress,port,getClient,loginWinLock);
		
		//�ȴ���¼���   ��¼��ɺ��¼����ر�  �������߳�
		synchronized (loginWinLock) {
			try {
				loginWinLock.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//��ȡ�����б��ϣ��������ѽ���
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
		
		//��ȡ�����б�
		LoginMessage loginMsg = (LoginMessage)obj;
		List<User> friendslist = loginMsg.getFriendsList();
		System.out.println("һ��������:"+friendslist.size());
		
		//����˽�Ĵ��ڵ�����
		ProPCView proPCView = new ProPCView(new HashMap<String,PCView>());
		
		//��������������
		MainWindow mainwindow = new MainWindow(friendslist,proPCView,client);		
		
		//������������
		Map<String,List> mapList = new HashMap<String,List>();
		//��������������
		Map<String,Object> mapLock = new HashMap<String,Object>();
		
		//��������˽����Ϣ���̲߳�������Ϣ����
		List<ViewPChat> listViewPChat = new ArrayList<>(); 
		Object viewPCLock = new Object();
		mapList.put("listViewPChat",listViewPChat);
		mapLock.put("viewPCLock",viewPCLock);
		Thread viewPChat = new Thread(new HandlViewPChat(listViewPChat,viewPCLock,proPCView));
		viewPChat.start();
		
		//�����������˷��ص��߳�
		Thread handleServer = new Thread(new HandleServer(client,mapList,mapLock));
		handleServer.start();
		
		while(true){
			
		}
	}
}
