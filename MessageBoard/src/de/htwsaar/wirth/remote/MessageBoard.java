package de.htwsaar.wirth.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

import de.htwsaar.wirth.remote.model.interfaces.Message;

public interface MessageBoard extends Remote {
	
	public void newMessage(String msg, String username, UUID token) throws RemoteException;
	public void deleteMessage(Message msg, String username, UUID token) throws RemoteException;
	public void editMessage(Message msg, String username, UUID token) throws RemoteException;
	public void publish(Message msg, String username, UUID token) throws RemoteException;
	public List<Message> getMessages() throws RemoteException;
	public UUID registerClient(Notifiable client, String username, String password) throws RemoteException;

}
