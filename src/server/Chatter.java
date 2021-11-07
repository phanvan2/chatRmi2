package server;

import client.ChatClient3IF;


/**
 * 
 * @author phanvan2
 * @github https://github.com/phanvan2
 *
 */
public class Chatter {

	public String name;
	public String idUser ; 
	public ChatClient3IF client;
	
	
	//constructor
	public Chatter(String name, ChatClient3IF client, String idUser){
		this.name = name;
		this.idUser = idUser; 
		this.client = client;
	}

	
	//getters and setters
	public String getName(){
		return name;
	}
	public ChatClient3IF getClient(){
		return client;
	}
	public String getIdUser() {
		return this.idUser ; 
	}
	
	
}
