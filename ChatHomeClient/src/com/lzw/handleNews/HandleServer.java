package com.lzw.handleNews;

import java.io.IOException;

import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
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
		// TODO Auto-generated method stub
		Scanner scannerSocket = null;
		try {
			scannerSocket = new Scanner(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(true){
			String MsgStr = scannerSocket.nextLine();
			String strArr[] = null;
			List commList = null;
			Object commLock = null;
			if(MsgStr.startsWith("GetPrivatePersonChat")){
				strArr = MsgStr.split("#");
				ViewPChat pChat = new ViewPChat(strArr[1], strArr[2], strArr[3]);
				commList = mapList.get("listViewPChat");
				commLock = mapLock.get("viewPCLock");
				
				synchronized (commLock){
					commList.add(pChat);
					commLock.notify();
				}
			}
		}
	}

}
