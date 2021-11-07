package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.Vector;

import client.ChatClient3IF;

/**
 * 
 * @author phanvan2
 * @github https://github.com/phanvan2
 *
 */
public class ChatServer extends UnicastRemoteObject implements ChatServerIF {
	String line = "---------------------------------------------\n";
	private Vector<Chatter> chatters;
	private static final long serialVersionUID = 1L;
	private Vector<Group> groups = new Vector<Group>(); 
	//Constructor
	public ChatServer() throws RemoteException {
		super();
		chatters = new Vector<Chatter>(10, 1);
	}
	
	//-----------------------------------------------------------
	/**
	 * LOCAL METHODS
	 */	
	public static void main(String[] args) {
		startRMIRegistry();	
		String hostName = "localhost";
		String serviceName = "GroupChatService";
		
		if(args.length == 2){
			hostName = args[0];
			serviceName = args[1];
		}
		
		try{
			ChatServerIF hello = new ChatServer();
			Naming.rebind("rmi://" + hostName + "/" + serviceName, hello);
			System.out.println("Group Chat RMI Server is running...");
		}
		catch(Exception e){
			System.out.println("Server had problems starting");
		}	
	}

	
	/**
	 * Start the RMI Registry
	 */
	public static void startRMIRegistry() {
		try{
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			System.out.println("RMI Server ready");
		}
		catch(RemoteException e) {
			e.printStackTrace();
		}
	}
		
	
	//-----------------------------------------------------------
	/*
	 *   REMOTE METHODS
	 */
	
	/**
	 * Return a message to client
	 */
	public String sayHello(String ClientName) throws RemoteException {
		System.out.println(ClientName + " sent a message");
		return "Hello " + ClientName + " from group chat server";
	}
	

	/**
	 * Send a string ( the latest post, mostly ) 
	 * to all connected clients
	 */
	public void updateChat(String name, String nextPost) throws RemoteException {
		String message =  name + " : " + nextPost + "\n";
		sendToAll(message);
	}
	
	/**
	 * Receive a new client remote reference
	 */
	@Override
	public void passIDentity(RemoteRef ref) throws RemoteException {	
		//System.out.println("\n" + ref.remoteToString() + "\n");
		try{
			System.out.println(line + ref.toString());
		}catch(Exception e){
			e.printStackTrace();
		}
	}//end passIDentity

	
	/**
	 * Receive a new client and display details to the console
	 * send on to register method
	 */
	@Override
	public void registerListener(String[] details) throws RemoteException {	

		registerChatter(details);
	}

	
	/**
	 * register the clients interface and store it in a reference for 
	 * future messages to be sent to, ie other members messages of the chat session.
	 * send a test message for confirmation / test connection
	 * @param details
	 */
	private void registerChatter(String[] details){		
		try{
			ChatClient3IF nextClient = ( ChatClient3IF )Naming.lookup("rmi://" + details[1] + "/" + details[2]);
			Chatter newchatter = new Chatter(details[0], nextClient, details[3]) ; 
			String[] currentUsers = getUserList();	
			String[] idUsers = getIdUserList(); 
			for (int i = 0 ; i < currentUsers.length ; i ++ ) {
				newchatter.getClient().updateUserList(currentUsers[i], idUsers[i]); 
			}
			
			
			chatters.addElement(newchatter);
			System.out.println("idUser: " + details[3]);
			
			sendToAll("[Server] : " + details[0] + " has joined the group.\n");
			
			updateUserList();		
		}
		catch(RemoteException | MalformedURLException | NotBoundException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Update all clients by remotely invoking their
	 * updateUserList RMI method
	 */
	private void updateUserList() {
		String[] currentUsers = getUserList();
		String[] idUsers = getIdUserList(); 

		for(Chatter c : chatters){
			try {
				c.getClient().updateUserList(currentUsers[currentUsers.length-1], idUsers[idUsers.length -1]);
			} 
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}	
	}
	

	/**
	 * generate a String array of current users
	 * @return
	 */
	private String[] getUserList(){
		// generate an array of current users
		String[] allUsers = new String[chatters.size()];
		for(int i = 0; i< allUsers.length; i++){
			allUsers[i] = chatters.elementAt(i).getName();
		}
		return allUsers;
	}
	
	private String[] getIdUserList(){
		// generate an array of current users
		String[] allUsers = new String[chatters.size()];
		for(int i = 0; i< allUsers.length; i++){
			allUsers[i] = chatters.elementAt(i).getIdUser();
		}
		return allUsers;
	}
	
	@Override
	public void createGroup(Vector<String> members, String nameGroup) throws RemoteException {
		String idGroup = "group" + Math.random() + "" + nameGroup; 
		Vector<Chatter> ch = new Vector<Chatter>() ;
		for (String mem : members) {
			Chatter c = findChatter(mem); 
			ch.add(c) ;
			c.getClient().updateUserList(nameGroup, idGroup);
		}
		groups.add(new Group(idGroup, ch));
	}
	

	/**
	 * Send a message to all users
	 * @param newMessage
	 */
	public void sendToAll(String newMessage){	
//		for(Chatter c : chatters){
//			try {
//				c.getClient().messageFromServer(newMessage);
//			} 
//			catch (RemoteException e) {
//				e.printStackTrace();
//			}
//		}	
	}

	
	/**
	 * remove a client from the list
	 */
	@Override
	public void leaveChat(String idUser) throws RemoteException{
		
//		for(Chatter c : chatters){
//			if(!c.getIdUser().equals(idUser)){
//				chatters.remove(c);
//				c.getClient().removeListUser(idUser);
//				break;
//			}
//		}		
//		if(!chatters.isEmpty()){
//			updateUserList();
//		}			
	}
	

	/**
	 * A method to send a private message to selected clients
	 * The integer array holds the indexes (from the chatters vector) 
	 * of the clients to send the message to
	 */
	@Override
	public void sendPM(int[] privateGroup, String privateMessage) throws RemoteException{
//		Chatter pc;
//		for(int i : privateGroup){
//			pc= chatters.elementAt(i);
//			pc.getClient().messageFromServer(privateMessage);
//		}
	}
	
	@Override
	public void sendPP(String idUser, String privateMessage, String idUser1) throws RemoteException{
		//Chatter pc;
		for (Chatter chatter : chatters) {
			// check idUser receive . if true , update UI client receive 
			if(chatter.getIdUser().equals(idUser)) {
				// update ui client receive
				chatter.getClient().messageFromServer(privateMessage, idUser1);
			}
		}

	}
	@Override
	public void sendGroup(String idGroup, String mess, String idSender) throws RemoteException{
		for (Group gr : groups) {
			if(gr.getIdGroup().equals(idGroup)) {
				for (Chatter chatter : gr.getChatter()) {
					if(! idSender.equals(chatter.getIdUser()))
					chatter.getClient().messageFromServer(mess, idGroup);
					
				}
			}
			break ; 
		}
	}
	
	public Chatter findChatter(String username) {
		for (Chatter chatter : chatters) {
			if(chatter.getName().equals(username))
				return chatter;
		}
		return null ; 
	}

	
}



