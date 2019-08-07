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
import com.lzw.jdbc.DBUtil;
import com.lzw.jdbcbean.User;
import com.lzw.newsbean.LoginMessage;
import com.lzw.newsbean.LoginNew;
import com.lzw.tool.MD5Utils;

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
				System.out.println("username:"+userName);
				Socket socket = loginNew.getSocket();
				PrintStream printStream = null;
				try {
					printStream = new PrintStream(socket.getOutputStream(),true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				 Connection conn = DBUtil.getConnection();
				 String sql = "select * from user where username = ?";
				 Object uname = userName;
				 ResultSet rs = DBUtil.executeQuery(conn,sql,uname);
				 
				 //获取查询结果
				 String JDBCpass = null;
				 Integer id = 1;
				 try {
					rs.next();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				 try {
					 JDBCpass = rs.getString(4);
					 id = rs.getInt(1);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				if(MD5Utils.md5(loginNew.getPassword()).equals(JDBCpass)){
					printStream.println("login succeed");
					System.out.println("用户:"+userName+"登录成功");
					
					//查询用户好友列表信息并发送到客户端
					List<User> userFriends = new ArrayList<User>();
					sql = "select B.id,B.nickname,B.username,B.password,B.state,B.regdate from (select * from friend where u_id = ?) AS A left join user AS B on friend_id = id;";
					rs = DBUtil.executeQuery(conn,sql,id);
					
				    try {
						while(rs.next()){
							User temp_user = new User(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getInt(5),rs.getString(6));
							userFriends.add(temp_user);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				    
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