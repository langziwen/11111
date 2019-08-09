package com.lzw.handleNews;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.lzw.dao.UserDao;
import com.lzw.jdbc.DBUtil;
import com.lzw.jdbcbean.User;
import com.lzw.newsbean.LoginMessage;
import com.lzw.newsbean.LoginNew;
import com.lzw.tool.MD5Utils;
import com.lzw.tool.SpringApplication;

public class HandleLogin implements Runnable{
	
	private Map<String,List> mapList;
	private List<LoginNew> listLoginNews;
	private Object loginLock;
	Map<String,Object> lockList;
	
	public HandleLogin(List<LoginNew> listLoginNews,Object loginLock,Map<String,List> mapList,Map<String,Object> lockList) {
		super();
		this.listLoginNews = listLoginNews;
		this.loginLock = loginLock;
		this.mapList = mapList;
		this.lockList = lockList;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
				synchronized (loginLock) {
					if(listLoginNews.size()==0){
						try {
							loginLock.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			//消息队列不为空
			if(listLoginNews.size()!=0){
				LoginNew loginNew = listLoginNews.get(0);
				listLoginNews.remove(0);
				String userName = loginNew.getUserName();
				Socket socket = loginNew.getSocket();
				PrintStream printStream = null;
				try {
					printStream = new PrintStream(socket.getOutputStream(),true);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//获取持久层对象
				ApplicationContext ctx = SpringApplication.getApplicationContext();
				UserDao userDao = (UserDao)ctx.getBean("userDao");
				User tempUser = userDao.findByUserName(userName);
				
				if(MD5Utils.md5(loginNew.getPassword()).equals(tempUser.getPassword())){
					printStream.println("login succeed");
					System.out.println("用户:"+userName+"登录成功");
					
					//查询用户好友列表信息并发送到客户端
					List<User> userFriends = userDao.findFriendsById(tempUser.getId());
				    LoginMessage loginMsg = new LoginMessage();
				    loginMsg.setFriendsList(userFriends);
				    
				    //对象序列化IO流
				    ObjectOutputStream  os = null;
				    try {
						os = new ObjectOutputStream(socket.getOutputStream());
						os.writeObject(loginMsg);
						os.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				    
					//保存入登录用户容器
					Map<String,Socket> mapSocket = loginNew.getMapSocket();
					mapSocket.put(userName,loginNew.getSocket());
					
					//创建处理客户端接收消息线程
					Thread HandleReadFromClientThread = new Thread(new HandleReadFromClient(userName, socket,mapList,lockList));
					HandleReadFromClientThread.start();
				}else{
					printStream.println("login failed");
				}
			}
		}
	}

}