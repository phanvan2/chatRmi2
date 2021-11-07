package Gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import client.ChatClient3;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.CardLayout;
import java.awt.GridLayout;
import javax.swing.border.LineBorder;

import Class.PacketUser;

/**
 * 
 * @author phanvan2
 * @github https://github.com/phanvan2
 *
 */
public class GuiClient1 extends JFrame {

	private JPanel contentPane;
	CardLayout card  = new CardLayout();
	JPanel panelViewChat ; 
	
	private JPanel panelinFor, panelCreateGroup, panel_1, panel_2;
	private JLabel lbCurrentUser , lblMember;
	private JTextField txtNameGroup ; 
	private JButton btnCreateGroup , btnAddMember; 
	
	private String username ; 
	private ChatClient3 chatClient;
	private String idUser ; 
	private Vector<PacketUser> vtUser = new Vector<PacketUser>();
	private Vector<GuiMainChat> vtmainchats = new Vector<GuiMainChat>() ;  
	JList listUser ; 
	private JTextField txtnputMember;
	
	private Vector<String> vtGroup = new Vector<String>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String s = JOptionPane.showInputDialog(null, "Type your name") ; 
					GuiClient1 frame = new GuiClient1(s);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GuiClient1(String username) {
		this.setTitle("Chat Rmi");
		this.username = username ; 
		this.idUser = Math.random() + "" + this.username ;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelUsers = new JPanel();
		panelUsers.setPreferredSize(new Dimension(100,200));
		contentPane.add(panelUsers, BorderLayout.WEST);
		listUser =  new JList(vtUser);
		listUser.updateUI();
		listUser.setCellRenderer(new UserCell());
		listUser.addMouseListener(mouseClicktoShowChat());
		panelUsers.setLayout(new BorderLayout(100, 100));
	
	
		
		JScrollPane scrollPane = new JScrollPane(listUser);
		panelUsers.add(scrollPane);
		
		panelViewChat= new JPanel();
		contentPane.add(panelViewChat, BorderLayout.CENTER);
		panelViewChat.setLayout(card);
		
		panelinFor = new JPanel();
		panelinFor.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPane.add(panelinFor, BorderLayout.NORTH);
		panelinFor.setLayout(new BorderLayout(0, 0));
		
		panel_1 = new JPanel();
		panelinFor.add(panel_1, BorderLayout.WEST);
		panel_1.setLayout(new GridLayout(1, 0, 0, 0));
		
		lbCurrentUser = new JLabel(this.username);
		panel_1.add(lbCurrentUser);
		
		panelCreateGroup = new JPanel();
		panelinFor.add(panelCreateGroup, BorderLayout.EAST);
		panelCreateGroup.setLayout(new GridLayout(3,2, 5,10));
		
		txtNameGroup = new JTextField();
		panelCreateGroup.add(txtNameGroup);
		txtNameGroup.setColumns(10);
		
		btnCreateGroup = new JButton("create");
		panelCreateGroup.add(btnCreateGroup);
		btnCreateGroup.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					chatClient.serverIF.createGroup(vtGroup, txtNameGroup.getText());
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		txtnputMember = new JTextField();
		panelCreateGroup.add(txtnputMember);
		txtnputMember.setColumns(10);
		
		btnAddMember = new JButton("Add Member");
		btnAddMember.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String s = txtnputMember.getText();
				txtnputMember.setText("");
				vtGroup.add(s);
				lblMember.setText(lblMember.getText() + " | " + s);
			}
		});
		panelCreateGroup.add(btnAddMember);
		
		lblMember = new JLabel("");
		panelCreateGroup.add(lblMember);
		try {
			getConnected(this.username);
			//addCardAndUser("public chat", "111public"); 

		} catch (RemoteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		} 
		
		this.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		     
			    	try {
			        	chatClient.serverIF.leaveChat(idUser);
					} catch (RemoteException e) {
						e.printStackTrace();
					}		        	
		       
		        System.exit(0);  
		    }   
		});
		
	}
	
	/**
	 *
	 * @param nameUser
	 * @param idUser
	 */
	public void addCardAndUser(String nameUser, String idUser) {
		
		PacketUser packett =  new PacketUser(nameUser, new JLabel(nameUser)) ;
		vtUser.add(packett);
		GuiMainChat mainchat = new GuiMainChat(nameUser, idUser, this.chatClient, packett) ; 
		vtmainchats.add(mainchat); 
		panelViewChat.add(nameUser, mainchat ) ; 
		listUser.updateUI();
	}
	

	public void removeCardUser(String nameUser, String idUser) {
		vtUser.remove((Object) nameUser);
		listUser.updateUI();
	}
	
	// action click user to show chat 
	public MouseListener mouseClicktoShowChat() {
		MouseListener mouseLissten = new  MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				JList theList = (JList) e.getSource(); 

				int index = theList.locationToIndex(e.getPoint()); 
				PacketUser selectpacket = (PacketUser)theList.getModel().getElementAt(index);
				selectpacket.getLblUser().setForeground(Color.black);
				card.show(panelViewChat, selectpacket.getName());
						
				
			}
			
		};
		return mouseLissten ; 
	}
	
	private void getConnected(String userName) throws RemoteException{
		//remove whitespace and non word characters to avoid malformed url
		String cleanedUserName = userName.replaceAll("\\s+","_");
		cleanedUserName = userName.replaceAll("\\W+","_");
		try {		
			chatClient = new ChatClient3(this, cleanedUserName, this.idUser, listUser );
			chatClient.startClient();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public Vector<GuiMainChat> getGuiChat(){
		return this.vtmainchats ; 
	}

	public String getIdUser() {
		return this.idUser ; 
	}
}

class UserCell implements ListCellRenderer{

	@Override
	public JPanel getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,boolean cellHasFocus) {
		// TODO Auto-generated method stub
		
		JPanel panel = new JPanel();
	
		panel.setLayout(new BorderLayout());
		PacketUser packet = (PacketUser) value; 
		panel.add(packet.getLblUser(), BorderLayout.CENTER);
	
		  if (isSelected) {
		      panel.setBackground(new Color(59, 148, 237));
		      packet.getLblUser().setForeground(Color.black);
		  } else { // when don't select
		       
		  }
		return panel;
	}
	
	
}


