package com.lzw.handleNews;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import com.lzw.newsbean.PrivatePersonChat;
import com.lzw.tool.MyOutputStream;

public class HandlePPChat implements Runnable{
	
	private Map<String,Socket> mapSocket;
	private List<PrivatePersonChat> ppcList;
	private Object ppChatLock;
	
	
	public HandlePPChat(Map<String, Socket> mapSocket, List<PrivatePersonChat> ppcList, Object ppChatLock) {
		super();
		this.mapSocket = mapSocket;
		this.ppcList = ppcList;
		this.ppChatLock = ppChatLock;
	}


	@Override
	public void run() {
		
		while(true){
			synchronized (ppChatLock) {
				if(ppcList.size()==0){
					try {
						ppChatLock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
			if(ppcList.size() != 0){
				PrivatePersonChat ppChat = ppcList.get(0);
				ppcList.remove(0);
				//获取对应的socket链接
				String ToUser = ppChat.getMessageToUser();
				Socket client = mapSocket.get(ToUser);
				ObjectOutputStream objectOutputStream = null;
				MyOutputStream myStream = null;
				try {
					if(ppChat.getNum() == 0){
						//第一次写入
						objectOutputStream = new ObjectOutputStream(client.getOutputStream());
						objectOutputStream.writeObject(ppChat);
					}else{
						myStream = new MyOutputStream(client.getOutputStream());
						myStream.writeObject(ppChat);
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}

}







