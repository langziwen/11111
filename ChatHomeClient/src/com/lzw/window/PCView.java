package com.lzw.window;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

import com.lzw.tool.UserUtil;

public class PCView extends JFrame{
	
	//�û���Ϣ
	private String nickname;
	private String username;
	private Socket client;
	
	//�����ڲ��࣬��ť�����¼�
	private class SendListener implements ActionListener{
		
		private String username;
		private Socket client;
		
		public SendListener(String username,Socket client){
			this.username = username;
			this.client = client;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String jta2_str = null;
			jta2_str = jta2.getText();
			jta1Append(jta2_str);
			
			String send_str = "PrivatePersonChat#"+UserUtil.getUserName()+"#"+username+"#"+jta2_str;
			PrintStream printstream = null;
			try {
				printstream = new PrintStream(client.getOutputStream());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			printstream.println(send_str);
		}
		
	}
	
	//�ϲ����
	JPanel jp1;
	JTextArea jta1;			//�����ı���
	JScrollPane jspane1;	//�����������
	JTextArea jta2;	
	JScrollPane jspane2;
	JSplitPane jsp;	//�����ִ���
	//�²����
	JButton jb1,jb2;//���尴ť
	JPanel jp2;
	
	public PCView(String nickname,String username,Socket client){
		
		//�û���Ϣ��ʼ��
		this.nickname = nickname;
		this.username = username;
		this.client = client;
		
		//�ϲ����
		jp1 = new JPanel();
		
		jta1 = new JTextArea();
		jta1.setLineWrap(true);	//���ö����ı����Զ�����
		jspane1=new JScrollPane(jta1);	//������������
		
		jta2 = new JTextArea();
		jta2.setLineWrap(true);
		jspane2=new JScrollPane(jta2);
		
		jsp=new JSplitPane(JSplitPane.VERTICAL_SPLIT,jspane1,jspane2); //������ִ���
		jsp.setDividerLocation(200);	//���ò�ִ����Ƶ����ʼλ��
		jsp.setDividerSize(1);			//���÷�Ƶ����С

		//�²����
		jp2=new JPanel();
		jb1=new JButton("����");		//������ť
		jb2=new JButton("ȡ��");
		
		jb1.addActionListener(new SendListener(username,client));
		//���ò��ֹ���
		jp1.setLayout(new BorderLayout());
		jp2.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		//������
		jp1.add(jsp);
		jp2.add(jb1);
		jp2.add(jb2);
				
		this.add(jp1,BorderLayout.CENTER);
		this.add(jp2,BorderLayout.SOUTH);

		//���ô���ʵ��
		this.setTitle("����"+nickname);		//���ý������
		this.setSize(400, 350);				//���ý�������
		this.setLocation(200, 200);			//���ý����ʼλ��
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	//����������ͽ���һͬ�ر�
		this.setVisible(true);				//���ý�����ӻ�
	}
	
	public synchronized void jta1Append(String str){
		jta1.append(str);
	}
}









