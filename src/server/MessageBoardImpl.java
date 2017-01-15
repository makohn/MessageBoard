package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import api.MessageBoard;
import api.Notifiable;

public class MessageBoardImpl 
				extends UnicastRemoteObject 
				implements MessageBoard, Notifiable {

	private static final long serialVersionUID = -4613549994529764225L;

	private List<Notifiable> clients;
	private List<String> messages;
	
	protected MessageBoardImpl() throws RemoteException {
		super();
		clients = new ArrayList<Notifiable>();
		messages = new ArrayList<String>();
	}

	@Override
	public void newMessage(String text) throws RemoteException {
		messages.add(text);
		for(Notifiable n : clients) {
			n.notify(text);
		}
	}

	@Override
	public void register(Notifiable n) throws RemoteException {
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
