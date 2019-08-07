package com.lzw.handleNews;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import com.lzw.newsbean.PrivatePersonChat;

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
		// TODO Auto-generated method stub
		while(true){
			synchronized (ppChatLock) {
				if(ppcList.size()==0){
					try {
						ppChatLock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			if(ppcList.size() != 0){
				PrivatePersonChat ppChat = ppcList.get(0);
				ppcList.remove(0);
				String fromUser = ppChat.getMessageFromUser();
				String ToUser = ppChat.getMessageToUser();
				String message = ppChat.getMessage();
				Socket client = mapSocket.get(ToUser);
				PrintStream printStream = null;
				try {
					printStream = new PrintStream(client.getOutputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String strToUser = "GetPrivatePersonChat#"+fromUser+"#"+ToUser+"#"+message;
				printStream.println(strToUser);
			}
		}
	}

}







