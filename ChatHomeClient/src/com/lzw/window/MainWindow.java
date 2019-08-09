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

//自定义button按钮
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
	//顶部区域，暂无
	JPanel jtop;
	
	//中部区域，选项卡
	JTabbedPane jTab; // 选项卡窗格
	JPanel jFriends;   // 好友界面
	JPanel jGroup;    //群聊界面
	
	//底部区域
	JPanel jbuttom;
	
	//创建内部类，按钮监听事件
	private class openFriendListener implements ActionListener{
		//窗口集合
		private ProPCView proPCView;
		//用户信息
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
	
	
	//构造函数
	public MainWindow(List<User> friendslist,ProPCView proPCView,Socket client){
		//外部传入数据
		this.friendslist = friendslist;
		this.proPCView = proPCView;
		this.client = client;
		//创建顶部,暂无
		jtop = new JPanel();
		jtop.setSize(380,150);
		jtop.setLocation(0,0);
		//创建中部区域
		jTab = new JTabbedPane();
		jFriends = new JPanel();
		jGroup = new JPanel();
		jTab.add("好友",jFriends);
		jTab.addTab("群组", jGroup);
		jTab.setSize(380,450);
		jTab.setLocation(0,150);
		
		//遍历好友集合，创建按钮
		for(int i=0;i<friendslist.size();i++){
			User tempuser = friendslist.get(i);
			friendButton tempbutton = new friendButton(tempuser.getNickname());
			tempbutton.addActionListener(new openFriendListener(tempuser.getNickname(),tempuser.getUsername(),proPCView,client));
			jFriends.add(tempbutton);
		}
		
		//创建底部，暂无
		jbuttom = new JPanel();
		jbuttom.setSize(380,100);
		
		jGroup = new JPanel();
		this.setLayout(null);
		this.add(jtop);
		this.add(jTab);
		this.add(jbuttom);
		
		//设置当前窗口
        this.setSize(380,700);
        this.setTitle("聊天之家");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
	}
	
}




