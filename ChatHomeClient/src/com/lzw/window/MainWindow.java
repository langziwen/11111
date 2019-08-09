package com.lzw.window;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import com.lzw.jdbcbean.User;
import com.lzw.prothread.ProPCView;

//�Զ���button��ť
class friendButton extends JButton{

	private String nickname;
	
	public friendButton(String nickname) {
		super(nickname);
		this.nickname = nickname;
	}
}


public class MainWindow extends JFrame{
	
	private List<User> friendslist;
	private ProPCView proPCView;
	private Socket client;
	//������������
	JPanel jtop;
	
	//�в�����ѡ�
	JTabbedPane jTab; // ѡ�����
	JPanel jFriends;   // ���ѽ���
	JPanel jGroup;    //Ⱥ�Ľ���
	
	//�ײ�����
	JPanel jbuttom;
	
	//�����ڲ��࣬��ť�����¼�
	private class openFriendListener implements ActionListener{
		//���ڼ���
		private ProPCView proPCView;
		//�û���Ϣ
		private String nickname;
		private String username;
		private Socket client;
		
		
		public openFriendListener(String nickname,String username,ProPCView proPCView,Socket client){
			this.nickname = nickname;
			this.username = username;
			this.proPCView = proPCView;
			this.client = client;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			PCView temp_view = new PCView(nickname,username,client);
			proPCView.put(username,temp_view);
		}
	};
	
	
	//���캯��
	public MainWindow(List<User> friendslist,ProPCView proPCView,Socket client){
		//�ⲿ��������
		this.friendslist = friendslist;
		this.proPCView = proPCView;
		this.client = client;
		//��������,����
		jtop = new JPanel();
		jtop.setSize(380,150);
		jtop.setLocation(0,0);
		//�����в�����
		jTab = new JTabbedPane();
		jFriends = new JPanel();
		jGroup = new JPanel();
		jTab.add("����",jFriends);
		jTab.addTab("Ⱥ��", jGroup);
		jTab.setSize(380,450);
		jTab.setLocation(0,150);
		
		//�������Ѽ��ϣ�������ť
		for(int i=0;i<friendslist.size();i++){
			User tempuser = friendslist.get(i);
			friendButton tempbutton = new friendButton(tempuser.getNickname());
			tempbutton.addActionListener(new openFriendListener(tempuser.getNickname(),tempuser.getUsername(),proPCView,client));
			jFriends.add(tempbutton);
		}
		
		//�����ײ�������
		jbuttom = new JPanel();
		jbuttom.setSize(380,100);
		
		jGroup = new JPanel();
		this.setLayout(null);
		this.add(jtop);
		this.add(jTab);
		this.add(jbuttom);
		
		//���õ�ǰ����
        this.setSize(380,700);
        this.setTitle("����֮��");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
	}
	
}




