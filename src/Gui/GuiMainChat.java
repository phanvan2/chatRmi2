package Gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import client.ChatClient3;

import javax.swing.JButton;

public class GuiMainChat extends JPanel implements ActionListener {
	
	public Vector<String> vtMess = new Vector<String>();
	public JList listMess ; 
	private JTextField txtInputMess;
	JButton btnSendMess ; 
	
	private ChatClient3 chatClient;
	public String username = ""; 
	public  String idUser = "" ;
	public String idUserReceive = ""; 
	public boolean group ; // if true => group , if false => people
	
	/**
	 * Create the panel.
	 */
	public GuiMainChat(String username, String idUserReceive, ChatClient3 chatClient) {
		this.username = username ; 
		this.idUser = chatClient.getIdUser() ; 
		this.chatClient = chatClient; 
		this.idUserReceive = idUserReceive ; 
		this.setLayout(new BorderLayout());
		vtMess.add("hello"); 
		vtMess.add(""+  idUserReceive); 
		listMess =  new JList(vtMess);
		listMess.updateUI();
		listMess.setCellRenderer(new MessCell());
	
	
		
		JScrollPane scrollPane = new JScrollPane(listMess);
		this.add(scrollPane);
		
		JPanel panelControl = new JPanel();
		add(panelControl, BorderLayout.SOUTH);
		panelControl.setLayout(new BorderLayout(0, 0));
		
		txtInputMess = new JTextField();
		panelControl.add(txtInputMess, BorderLayout.CENTER);
		txtInputMess.setColumns(10);
		
		btnSendMess = new JButton("Send");
		btnSendMess.addActionListener(this);
		panelControl.add(btnSendMess, BorderLayout.EAST);
		
		this.group = checkGroup(); 

	}
	public static void main(String[] args) {
		JFrame f = new JFrame("chat");
		f.setSize(400,200);
		f.getContentPane().setLayout(new BorderLayout());
		//f.getContentPane().add(new GuiMainChat("ah", "12"));
		f.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btnSendMess){
			System.out.println("send"); 
			String message = txtInputMess.getText();
			txtInputMess.setText("");
			try {
				sendPrivate(idUserReceive ,message);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("Sending message : " + message);
		}
	}
	
	private void sendMessage(String chatMessage) throws RemoteException {
		chatClient.serverIF.updateChat(username, chatMessage);
	}
	
	/**
	 * send from a people
	 * @param idUser  id receiver
	 * @param message
	 * @throws RemoteException
	 */
	private void sendPrivate(String idUser, String message) throws RemoteException {
		String privateMessage = chatClient.name + ": " + message + "\n";
		String mess =  message + ": Me" ; 
		vtMess.add(mess);
		listMess.updateUI();
		if(!group)
			chatClient.serverIF.sendPP(idUser, privateMessage, chatClient.getIdUser() );
		else 
			chatClient.serverIF.sendGroup(idUser, privateMessage,  chatClient.getIdUser());

	}
	
	public Boolean checkGroup() {
		if(this.idUserReceive.contains("group"))
			return true  ;
		return false  ; 
	}



	
}
// set list mess display
class MessCell implements ListCellRenderer{

	@Override
	public JPanel getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,boolean cellHasFocus) {
		// TODO Auto-generated method stub
		
		JPanel panel = new JPanel();
	
		panel.setLayout(new BorderLayout());
		String s = value.toString();
	
		if( s.contains("Me")) {
			JLabel text = new JLabel(s); 
			panel.add(text, BorderLayout.EAST);
		}else {
			JLabel text = new JLabel(s); 
			panel.add(text, BorderLayout.CENTER);
		}
		

		return panel;
	}
	
	
}
