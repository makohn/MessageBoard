package de.htwsaar.server.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import de.htwsaar.remote.remote.MessageBoard;
import de.htwsaar.remote.remote.NotifiableServer;

public class MessageBoardImpl 
				extends UnicastRemoteObject 
				implements MessageBoard, NotifiableServer {

	private static final long serialVersionUID = -4613549994529764225L;

	private List<NotifiableServer> clients;
	private List<String> messages;
	
	protected MessageBoardImpl() throws RemoteException {
		super();
		clients = new ArrayList<NotifiableServer>();
		messages = new ArrayList<String>();
	}

	@Override
	public void newMessage(String text) throws RemoteException {
		messages.add(text);
		for(NotifiableServer n : clients) {
			n.notify(text);
		}
	}

	@Override
	public void register(NotifiableServer n) throws RemoteException {
		clients.add(n);
		
	}

	@Override
	public List<String> listMessages() throws RemoteException {
		return messages;
	}

	@Override
	public void notify(String message) throws RemoteException {
		newMessage(message);
	}

}
