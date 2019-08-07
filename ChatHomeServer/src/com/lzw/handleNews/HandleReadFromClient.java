package com.lzw.handleNews;

import java.io.IOException;


import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import com.lzw.newsbean.PrivatePersonChat;;

public class HandleReadFromClient implements Runnable{
	
	private String userName;
	private Socket socket;
	private Map<String,List> mapList;
	private Map<String,Object> lockList;
	
	public HandleReadFromClient(String userName, Socket socket,Map<String,List> mapList,Map<String,Object> lockList) {
		super();
		this.userName = userName;
		this.socket = socket;
		this.mapList = mapList;
		this.lockList = lockList;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Scanner scannerRU = null;
		PrintStream printstram = null;
		try {
			scannerRU = new Scanner(socket.getInputStream());
			printstram = new PrintStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(userName+"的线程已经启动");
		while(true){
			String Msg = scannerRU.nextLine();
			String msgArr[] = null;
			
			if(Msg.startsWith("PrivatePersonChat")){
				
				msgArr = Msg.split("#");
				PrivatePersonChat ppc = new PrivatePersonChat(msgArr[0],msgArr[1],msgArr[2],msgArr[3]);
				Object ppcLock = lockList.get("ppChatLock");
				//获取私聊消息队列
				List<PrivatePersonChat> ppcList = mapList.get("PrivatePersonChat");
				
				synchronized (ppcLock){
					ppcList.add(ppc);
					ppcLock.notify();
				}
			}else if(Msg.startsWith("GroupPersonChat")){
				
				msgArr = Msg.split("@");
			}else if(Msg.startsWith("logout")){
				
				msgArr = Msg.split("@");
			}
		}
	}

}








