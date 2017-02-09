package de.htwsaar.wirth.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

import de.htwsaar.wirth.remote.model.interfaces.Message;

public interface MessageBoard extends Remote {
	
	void newMessage(String msg, String username, UUID token) throws RemoteException;
	void deleteMessage(Message msg, String username, UUID token) throws RemoteException;
	void editMessage(Message msg, String username, UUID token) throws RemoteException;
	void publish(Message msg, String username, UUID token) throws RemoteException;
	List<Message> getMessages() throws RemoteException;
	UUID registerClient(Notifiable client, String username, String password) throws RemoteException;

}
