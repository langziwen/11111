package com.lzw.window;

import java.awt.Graphics;

import com.lzw.tool.UserUtil;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.lzw.jdbcbean.User;
import com.lzw.newsbean.LoginNew;

//消息提示面板
class MessageJPanel extends JPanel{
	
	private int flag = 0;
	
	public void paint(Graphics g){
		if(flag == 0){
			g.drawString("用户名或密码错误",200,40);
		}else if(flag == 1){
			g.drawString("用户名不能为空",200,40);
		}else if(flag == 2){
			g.drawString("密码不能为空",200,40);
		}
	}
	
	public void setFlag(int flag) {
		this.flag = flag;
	}
}

public class LoginWindow extends JFrame{
	
	private String serverAddress;
	private Integer port;
    private List<Socket> getClient;
	private Object loginWinLock;
	
	//四块面板
	JPanel jp0,jp1, jp2, jp3;
	//两个输入框
    JLabel jlb1, jlb2;
    //登录和注册按钮
    JButton jb1, jb2;
    //操作文本
    JTextField juf1;
    JPasswordField jpf1;
    
    //内部类
    private class LoginListener implements ActionListener{
    	private String serverAddress;
    	private Integer port;
    	private List<Socket> getClient;
    	private Object loginWinLock;
    	
    	public LoginListener(String serverAddress,Integer port,List<Socket> getClient,Object loginWinLock){
    		this.serverAddress = serverAddress;
    		this.port = port;
    		this.getClient = getClient;
    		this.loginWinLock = loginWinLock;
    	}
    	
    	//处理登录业务
		@Override
		public void actionPerformed(ActionEvent e) {
			
			//获取用户名和密码
			String username = juf1.getText();
			String password = jpf1.getText();
			//PrintStream printstram = null;
			Scanner scanner = null;
			
			//创建消息对象
			LoginNew loginNew = new LoginNew();
			loginNew.setUserName(username);
			loginNew.setPassword(password);
			
			Socket client = null;
			ObjectInputStream is = null;
			ObjectOutputStream os = null;
			try {
				 //获取客户端链接
				 client = new Socket(serverAddress,port);
				 scanner = new Scanner(client.getInputStream());
				
				//获取对象io流     先创建输出流 在创建输入流  否则会出现死锁
				os = new ObjectOutputStream(client.getOutputStream());
				is = new ObjectInputStream(client.getInputStream());
				
				os.writeObject(loginNew);
				os.flush();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			//接受消息对象
			String msgFromServer = scanner.nextLine();
			if(msgFromServer.equals("login succeed")){
				System.out.println("登录已经成功，唤醒主线程");
				getClient.add(client);
				
				//将当前用户账号存入currentUser
				UserUtil.setUserName(username);
				synchronized (loginWinLock) {
					loginWinLock.notify();
				}
				dispose();
			}else{
				
			}
			
		}
    	
    }
    
    //构造函数创建出一个窗口
    public LoginWindow(String serverAddress,Integer port,List<Socket> getClient,Object loginWinLock){
    	this.serverAddress = serverAddress;
    	this.port = port;
    	this.getClient = getClient;
    	this.loginWinLock = loginWinLock;
    	jp0 = new MessageJPanel();
    	jp1 = new JPanel();
        jp2 = new JPanel();
        jp3 = new JPanel();

        jlb1 = new JLabel("用户名");
        jlb2 = new JLabel("密    码");

        jb1 = new JButton("登录");
        jb2 = new JButton("取消");
        
      
        //向登录按钮添加事件
        jb1.addActionListener(new LoginListener(serverAddress,port,getClient,loginWinLock));
        
        juf1 = new JTextField(20);
        jpf1 = new JPasswordField(20);
        
        this.setLayout(new GridLayout(4, 1));
        
        //加入用户名  及文本框
        jp1.add(jlb1);
        jp1.add(juf1);

        //加入密码 及文本框
        jp2.add(jlb2);
        jp2.add(jpf1);

        //加入 登录和取消按钮
        jp3.add(jb1);
        jp3.add(jb2);
        
        //加入到JFrame
        this.add(jp0);
        this.add(jp1);
        this.add(jp2);
        this.add(jp3);
       
        //隐藏消息提示窗口
        jp0.setVisible(false);
        
        //设置当前窗口
        this.setSize(500, 300);
        this.setTitle("登录");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    
}





