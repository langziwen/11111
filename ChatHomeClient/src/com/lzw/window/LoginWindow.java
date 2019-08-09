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

//��Ϣ��ʾ���
class MessageJPanel extends JPanel{
	
	private int flag = 0;
	
	public void paint(Graphics g){
		if(flag == 0){
			g.drawString("�û������������",200,40);
		}else if(flag == 1){
			g.drawString("�û�������Ϊ��",200,40);
		}else if(flag == 2){
			g.drawString("���벻��Ϊ��",200,40);
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
	
	//�Ŀ����
	JPanel jp0,jp1, jp2, jp3;
	//���������
    JLabel jlb1, jlb2;
    //��¼��ע�ᰴť
    JButton jb1, jb2;
    //�����ı�
    JTextField juf1;
    JPasswordField jpf1;
    
    //�ڲ���
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
    	
    	//�����¼ҵ��
		@Override
		public void actionPerformed(ActionEvent e) {
			
			//��ȡ�û���������
			String username = juf1.getText();
			String password = jpf1.getText();
			//PrintStream printstram = null;
			Scanner scanner = null;
			
			//������Ϣ����
			LoginNew loginNew = new LoginNew();
			loginNew.setUserName(username);
			loginNew.setPassword(password);
			
			Socket client = null;
			ObjectInputStream is = null;
			ObjectOutputStream os = null;
			try {
				 //��ȡ�ͻ�������
				 client = new Socket(serverAddress,port);
				 scanner = new Scanner(client.getInputStream());
				
				//��ȡ����io��     �ȴ�������� �ڴ���������  ������������
				os = new ObjectOutputStream(client.getOutputStream());
				is = new ObjectInputStream(client.getInputStream());
				
				os.writeObject(loginNew);
				os.flush();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			//������Ϣ����
			String msgFromServer = scanner.nextLine();
			if(msgFromServer.equals("login succeed")){
				System.out.println("��¼�Ѿ��ɹ����������߳�");
				getClient.add(client);
				
				//����ǰ�û��˺Ŵ���currentUser
				UserUtil.setUserName(username);
				synchronized (loginWinLock) {
					loginWinLock.notify();
				}
				dispose();
			}else{
				
			}
			
		}
    	
    }
    
    //���캯��������һ������
    public LoginWindow(String serverAddress,Integer port,List<Socket> getClient,Object loginWinLock){
    	this.serverAddress = serverAddress;
    	this.port = port;
    	this.getClient = getClient;
    	this.loginWinLock = loginWinLock;
    	jp0 = new MessageJPanel();
    	jp1 = new JPanel();
        jp2 = new JPanel();
        jp3 = new JPanel();

        jlb1 = new JLabel("�û���");
        jlb2 = new JLabel("��    ��");

        jb1 = new JButton("��¼");
        jb2 = new JButton("ȡ��");
        
      
        //���¼��ť����¼�
        jb1.addActionListener(new LoginListener(serverAddress,port,getClient,loginWinLock));
        
        juf1 = new JTextField(20);
        jpf1 = new JPasswordField(20);
        
        this.setLayout(new GridLayout(4, 1));
        
        //�����û���  ���ı���
        jp1.add(jlb1);
        jp1.add(juf1);

        //�������� ���ı���
        jp2.add(jlb2);
        jp2.add(jpf1);

        //���� ��¼��ȡ����ť
        jp3.add(jb1);
        jp3.add(jb2);
        
        //���뵽JFrame
        this.add(jp0);
        this.add(jp1);
        this.add(jp2);
        this.add(jp3);
       
        //������Ϣ��ʾ����
        jp0.setVisible(false);
        
        //���õ�ǰ����
        this.setSize(500, 300);
        this.setTitle("��¼");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    
}





