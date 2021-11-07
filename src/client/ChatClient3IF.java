package client;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * 
 * @author phanvan2
 * @github https://github.com/phanvan2
 *
 */
public interface ChatClient3IF extends Remote{

	/**
	 * 
	 * @param message
	 * @param idUser1 id sender
	 * @throws RemoteException
	 */
	public void messageFromServer(String message, String idUser1) throws RemoteException;

	public void updateUserList(String currentUsers, String idUser) throws RemoteException;
	
	public void removeListUser(String idUser) throws RemoteException ;
}
/**
 * 
 * 
 * 
 *
 */