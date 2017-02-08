package de.htwsaar.wirth.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import de.htwsaar.wirth.remote.MessageBoard;
import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;

public class MessageBoardImpl extends UnicastRemoteObject implements Notifiable, MessageBoard, ParentServer {

	private String groupName;
	private List<Notifiable> serverList;
	private List<Notifiable> clientList;
	private ParentServer parent;

	private Map<String,UUID> sessions;

	public MessageBoardImpl(String groupName) throws RemoteException {
		this.groupName = groupName;
		clientList = Collections.synchronizedList(new ArrayList<>());
		serverList = Collections.synchronizedList(new ArrayList<>());
		sessions = new ConcurrentHashMap<>();

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

	@Override
	public void registerServer(Notifiable childServer) throws RemoteException {
		//TODO
	}


	@Override
	public void newMessage(String msg, String username, UUID token) throws RemoteException {

	}

	@Override
	public void deleteMessage(Message msg, String username, UUID token) throws RemoteException {

	}

	@Override
	public void editMessage(Message msg, String username, UUID token) throws RemoteException {

	}

	@Override
	public void publish(Message msg, String username, UUID token) throws RemoteException {

	}

	public List<Message> getMessages() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UUID registerClient(Notifiable client, String username, String password) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void notifyNew(Message msg) throws RemoteException {
		
	}

	@Override
	public void notifyDelete(Message msg) throws RemoteException {

	}

	@Override
	public void notifyEdit(Message msg) throws RemoteException {

	}
}
