package com.lzw.handleNews;

import java.util.List;

import com.lzw.newsbean.ViewPChat;
import com.lzw.prothread.ProPCView;
import com.lzw.window.PCView;

public class HandlViewPChat implements Runnable {
	
	private List<ViewPChat> listViewPChat;
	private Object viewPCLock;
	private ProPCView proPCView;
	
	public HandlViewPChat(List<ViewPChat> listViewPChat, Object viewPCLock,ProPCView proPCView) {
		super();
		this.listViewPChat = listViewPChat;
		this.viewPCLock = viewPCLock;
		this.proPCView = proPCView;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			synchronized (viewPCLock){
				if(listViewPChat.size()==0){
					try {
						viewPCLock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if(listViewPChat.size()!=0){
				ViewPChat pChat = listViewPChat.get(0);
				listViewPChat.remove(0);
				String userFrom = pChat.getFromUser();
				PCView pcview = proPCView.get(userFrom);
				String message = pChat.getMessage();
				
				System.out.println("接收到的消息为"+message+"  准备显示到界面上");
				pcview.jta1Append(message);
			}
		}
	}

}
