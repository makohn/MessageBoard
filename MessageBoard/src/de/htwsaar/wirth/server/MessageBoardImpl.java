package de.htwsaar.wirth.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import de.htwsaar.wirth.remote.MessageBoard;
import de.htwsaar.wirth.remote.NotifiableClient;
import de.htwsaar.wirth.remote.NotifiableServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.remote.model.interfaces.User;

public class MessageBoardImpl extends UnicastRemoteObject implements MessageBoard, NotifiableServer {

	protected MessageBoardImpl() throws RemoteException {
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = -4613549994529764225L;

	public void notifyServerDelete(Message msg) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	public void notifyServerEdit(Message msg) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	public void publish(Message msg) {
		// TODO Auto-generated method stub
		
	}

	public void newMessage(Message msg, User user) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	public void deleteMessage(Message msg, User user) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	public void editMessage(Message msg, User user) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	public void publish(Message msg, User user) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	public List<Message> getMessages() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public void register(NotifiableClient client) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	public void register(NotifiableServer server) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	
}
