package com.lzw.handleNews;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.lzw.newsbean.PrivatePersonChat;
import com.lzw.newsbean.ViewPChat;;

public class HandleServer implements Runnable {
	
	private Socket socket;
	Map<String,List> mapList;
	Map<String,Object> mapLock;
	
	
	public HandleServer(Socket socket, Map<String, List> mapList, Map<String, Object> mapLock) {
		super();
		this.socket = socket;
		this.mapList = mapList;
		this.mapLock = mapLock;
	}

	@Override
	public void run() {
		ObjectInputStream oi = null;
		try {
			oi = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(true){
			Object ob = null;
			try {
				ob = oi.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			String className = ob.getClass().getName();
			if("com.lzw.newsbean.PrivatePersonChat".equals(className)){
				PrivatePersonChat temp = (PrivatePersonChat)ob;
				//获取线程锁和消息队列
				List commList = null;
				Object commLock = null;
				commList = mapList.get("listViewPChat");
				commLock = mapLock.get("viewPCLock");
				
				synchronized (commLock){
					commList.add(temp);
					commLock.notify();
				}
			}
			
		}
	}

}
