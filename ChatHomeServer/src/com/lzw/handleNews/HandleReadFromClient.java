package com.lzw.handleNews;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import com.lzw.newsbean.PrivatePersonChat;;

public class HandleReadFromClient implements Runnable{
	
	private String userName;
	private Socket socket;
	private Map<String,List> mapList;
	private Map<String,Object> lockList;
	private Integer num;
	public HandleReadFromClient(String userName, Socket socket,Map<String,List> mapList,Map<String,Object> lockList) {
		super();
		this.userName = userName;
		this.socket = socket;
		this.mapList = mapList;
		this.lockList = lockList;
		num = 0;
	}

	@Override
	public void run() {
		 ObjectInputStream oi = null;
		 //������һ���߳����ظ�����������������
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String className = ob.getClass().getName();
			
			if("com.lzw.newsbean.PrivatePersonChat".equals(className)){
				PrivatePersonChat temp = (PrivatePersonChat)ob;
				//�ж��ǵڼ���д��
				temp.setNum(num);
				if(num == 0){
					num ++;
				}
				//��ȡ�߳�����׼������˽���߳�
				Object ppcLock = lockList.get("ppChatLock");
				//��ȡ˽����Ϣ����
				List<PrivatePersonChat> ppcList = mapList.get("PrivatePersonChat");
				synchronized (ppcLock){
					ppcList.add(temp);
					ppcLock.notify();
				}
			}
			
		}
	}

}








