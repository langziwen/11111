package com.lzw.handleNews;

import java.util.List;

import com.lzw.newsbean.PrivatePersonChat;
import com.lzw.prothread.ProPCView;
import com.lzw.window.PCView;

public class HandlViewPChat implements Runnable {
	
	private List<PrivatePersonChat> listViewPChat;
	private Object viewPCLock;
	private ProPCView proPCView;
	
	public HandlViewPChat(List<PrivatePersonChat> listViewPChat, Object viewPCLock,ProPCView proPCView) {
		super();
		this.listViewPChat = listViewPChat;
		this.viewPCLock = viewPCLock;
		this.proPCView = proPCView;
	}

	
	@Override
	public void run() {
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
				PrivatePersonChat pChat = listViewPChat.get(0);
				listViewPChat.remove(0);
				String userFrom = pChat.getMessageFromUser();
				//从容器中获取对应的用户窗口
				PCView pcview = proPCView.get(userFrom);
				String message = pChat.getMessage();
				//将信息显示到界面上
				pcview.jta1Append(message);
			}
		}
	}

}
