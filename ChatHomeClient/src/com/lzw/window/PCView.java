package com.lzw.window;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import com.lzw.newsbean.PrivatePersonChat;
import com.lzw.tool.MyOutputStream;
import com.lzw.tool.UserUtil;

public class PCView extends JFrame{
	
	//用户信息
	private String nickname;
	private String username;
	private Socket client;
	private ObjectOutputStream oi;
	private MyOutputStream oi2;
	private Integer num;
	//创建内部类，按钮监听事件
	private class SendListener implements ActionListener{
		
		private String username;
		private Socket client;
		
		public SendListener(String username,Socket client){
			this.username = username;
			this.client = client;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			String jta2_str = null;
			jta2_str = jta2.getText();
			jta1Append(jta2_str);
			
			PrivatePersonChat temp = new PrivatePersonChat();
			temp.setMessageFromUser(UserUtil.getUserName());
			temp.setMessageToUser(username);
			temp.setMessage(jta2_str);
			
			try {
				if(num != 0){
					//不是第一次写入，避免追加文件头
					oi2.writeObject(temp);
				}else{
					oi.writeObject(temp);
					num ++;
				}
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	//上部组件
	JPanel jp1;
	JTextArea jta1;			//定义文本域
	JScrollPane jspane1;	//定义滚动窗格
	JTextArea jta2;	
	JScrollPane jspane2;
	JSplitPane jsp;	//定义拆分窗格
	//下部组件
	JButton jb1,jb2;//定义按钮
	JPanel jp2;
	
	public PCView(String nickname,String username,Socket client){
		
		//用户信息初始化
		this.nickname = nickname;
		this.username = username;
		this.client = client;
		
		//上部组件
		jp1 = new JPanel();
		
		jta1 = new JTextArea();
		jta1.setLineWrap(true);	//设置多行文本框自动换行
		jspane1=new JScrollPane(jta1);	//创建滚动窗格
		
		jta2 = new JTextArea();
		jta2.setLineWrap(true);
		jspane2=new JScrollPane(jta2);
		
		jsp=new JSplitPane(JSplitPane.VERTICAL_SPLIT,jspane1,jspane2); //创建拆分窗格
		jsp.setDividerLocation(200);	//设置拆分窗格分频器初始位置
		jsp.setDividerSize(1);			//设置分频器大小

		//下部组件
		jp2=new JPanel();
		jb1=new JButton("发送");		//创建按钮
		jb2=new JButton("取消");
		
		jb1.addActionListener(new SendListener(username,client));
		//设置布局管理
		jp1.setLayout(new BorderLayout());
		jp2.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		//添加组件
		jp1.add(jsp);
		jp2.add(jb1);
		jp2.add(jb2);
				
		this.add(jp1,BorderLayout.CENTER);
		this.add(jp2,BorderLayout.SOUTH);

		//设置窗体实行
		this.setTitle("好友"+nickname);		//设置界面标题
		this.setSize(400, 350);				//设置界面像素
		this.setLocation(200, 200);			//设置界面初始位置
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	//设置虚拟机和界面一同关闭
		this.setVisible(true);				//设置界面可视化
		
		//记录第几次写入数据
		num = 0;
		try {
			//获取常规对象输出流
			oi = new ObjectOutputStream(client.getOutputStream());
			//自定义输出流
			oi2 = new MyOutputStream(client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void jta1Append(String str){
		str = str + "\n";
		jta1.append(str);
		//清空发送信息的面板
		jta2.setText("");
	}
}



