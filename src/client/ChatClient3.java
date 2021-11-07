package client;
import java.awt.Color;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JList;
import javax.swing.JOptionPane;

import Gui.GuiClient1;
import Gui.GuiMainChat;
import server.ChatServerIF;

/**
 * 
 * @author phanvan2
 * @github https://github.com/phanvan2
 *
 */
public class ChatClient3  extends UnicastRemoteObject implements ChatClient3IF {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7468891722773409712L;
	GuiMainChat chatGUI;
	GuiClient1 guiclient  ; 
	private String hostName = "localhost";
	private String serviceName = "GroupChatService";
	private String clientServiceName;
	public String name;
	private String idUser ; 
	public ChatServerIF serverIF;
	public JList jl; 
	protected boolean connectionProblem = false;

	
	/**
	 * class constructor,
	 * note may also use an overloaded constructor with 
	 * a port no passed in argument to super
	 * @throws RemoteException
	 */
	public ChatClient3(GuiClient1 guiclient, String userName, String idUser, JList jl ) throws RemoteException {
		super();
		this.guiclient = guiclient;
		this.name = userName;
		this.idUser = idUser;
		this.jl = jl ;
		this.clientServiceName = "ClientListenService_" + userName;
	}

	
	/**
	 * Register our own listening service/interface
	 * lookup the server RMI interface, then send our details
	 * @throws RemoteException
	 */
	public void startClient() throws RemoteException {		
		String[] details = {name, hostName, clientServiceName, idUser};	

		try {
			Naming.rebind("rmi://" + hostName + "/" + clientServiceName, this);
			serverIF = ( ChatServerIF )Naming.lookup("rmi://" + hostName + "/" + serviceName);	
		} 
		catch (Exception  e) {
//			JOptionPane.showMessageDialog(
//					chatGUI.frame, "The server seems to be unavailable\nPlease try later",
//					"Connection problem", JOptionPane.ERROR_MESSAGE);
			connectionProblem = true;
			e.printStackTrace();
		}
		
		if(!connectionProblem){
			registerWithServer(details);
		}	
		System.out.println("Client Listen RMI Server is running...\n");
	}


	/**
	 * pass our username, hostname and RMI service name to
	 * the server to register out interest in joining the chat
	 * @param details
	 */
	public void registerWithServer(String[] details) {		
		try{
			serverIF.passIDentity(this.ref);//now redundant ??
			serverIF.registerListener(details);			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	//=====================================================================
	/**
	 * Receive a string from the chat server
	 * this is the clients RMI method, which will be used by the server 
	 * to send messages to us
	 */
	@Override
	public void messageFromServer(String message, String idUser1) throws RemoteException {
		System.out.println( message );
		System.out.println("message From server in chat lient" + idUser1);
		
		for (GuiMainChat guichat : guiclient.getGuiChat()) {
			if(guichat.idUserReceive.equals(idUser1)) {
				guichat.vtMess.add(message);
				guichat.listMess.updateUI();
				guichat.packet.getLblUser().setForeground(Color.red);
				jl.updateUI();
			}

		}

	}

	/**
	 * A method to update the display of users 
	 * currently connected to the server
	 */
	@Override
	public void updateUserList(String currentUsers, String idUser) throws RemoteException {
		if(!this.idUser.equals(idUser))
		this.guiclient.addCardAndUser(currentUsers, idUser );
	}
	
	@Override
	public void removeListUser(String idUser) throws RemoteException {
		for (GuiMainChat g : guiclient.getGuiChat()) {
			if(g.idUserReceive.equals(idUser)) {
				guiclient.removeCardUser(g.username, idUser);
			}
		}	
	}
	
	public String getIdUser() {
		return this.idUser; 
	}

}//end class













