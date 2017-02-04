package de.htwsaar.wirth.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.remote.model.interfaces.User;

public interface MessageBoard extends Remote {
	
	public void newMessage(Message msg, User user) throws RemoteException;
	public void deleteMessage(Message msg, User user) throws RemoteException;
	public void editMessage(Message msg, User user) throws RemoteException;
	public void publish(Message msg, User user) throws RemoteException;
	public List<Message> getMessages() throws RemoteException;
	public void register(NotifiableClient client) throws RemoteException;
	public void register(NotifiableServer server) throws RemoteException;
}
