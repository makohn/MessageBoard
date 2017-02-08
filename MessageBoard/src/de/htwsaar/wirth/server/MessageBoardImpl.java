package de.htwsaar.wirth.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import de.htwsaar.wirth.remote.MessageBoard;
import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.MessageImpl;
import de.htwsaar.wirth.remote.model.interfaces.Message;


public class MessageBoardImpl extends UnicastRemoteObject implements Notifiable, MessageBoard, ParentServer {

	private String groupName;
	private List<Notifiable> serverList;
	private List<Notifiable> clientList;
	private ParentServer parent;

	private Map<String,UUID> sessions;

	public MessageBoardImpl(String groupName) throws RemoteException {
		this.groupName = groupName;
		clientList = Collections.synchronizedList(new ArrayList());
		serverList = Collections.synchronizedList(new ArrayList());
		sessions = new ConcurrentHashMap();

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


	public void registerServer(Notifiable childServer) throws RemoteException {
		//TODO
	}


	/**
	 * NewMessage is capsuled in the MessageBoard-Interface. This method can be called by the clients
	 * to submit a new message to their group-server. To authenticate the User a username and
	 * a valid authentication token is required.
	 * @param msg
	 * @param username
	 * @param token
	 * @throws RemoteException
	 */
	public void newMessage(String msg, String username, UUID token) throws RemoteException {
			Message message = new MessageImpl(msg,username,groupName,false);
			notifyNew(message);
	}

	/**
	 * deleteMessage is capsuled in the MessageBoard-Interface. This method can be called by the admin
	 * to delete a existing message on their group-server.
	 * To authenticate the User a username and a valid authentication token is required.
	 * @param msg
	 * @param username
	 * @param token
	 * @throws RemoteException
	 */
	public void deleteMessage(Message msg, String username, UUID token) throws RemoteException {

	}

	/**
	 * editMessage is capsuled in the MessageBoard-Interface. This method can be called by the author of a message
	 * to edit the existing message on their group-server.
	 * In case the message was published the change effects also all
	 * other servers which received this published message.
	 * To authenticate the User a username and a valid authentication token is required.
	 * @param msg
	 * @param username
	 * @param token
	 * @throws RemoteException
	 */
	public void editMessage(Message msg, String username, UUID token) throws RemoteException {

	}

	/**
	 * publish is capsuled in the MessageBoard-Interface.
	 * This method can be called by the admin to push a existing message to the parent-server.
	 * To authenticate the User a username and a valid authentication token is required.
	 * @param msg
	 * @param username
	 * @param token
	 * @throws RemoteException
	 */
	public void publish(Message msg, String username, UUID token) throws RemoteException {

	}

	/**
	 * getMessages is capsuled in the MessageBoard-Interface and in the ParentServer-Interface.
	 * This method can be called by a client to initialise his messageboard or it can called by a
	 * just new born server which want to receive all messages from his parent .
	 * @return
	 * @throws RemoteException
	 */
	public List<Message> getMessages() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}


	public UUID registerClient(Notifiable client, String username, String password) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}



	public void notifyNew(Message msg) throws RemoteException {
		
	}


	public void notifyDelete(Message msg) throws RemoteException {

	}


	public void notifyEdit(Message msg) throws RemoteException {

	}

	protected void addParent(ParentServer parent){
			this.parent = parent;
	}
}
