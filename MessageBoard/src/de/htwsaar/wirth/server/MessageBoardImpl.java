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


	/**
	 *publish is capsuled in the ParentServer-Interface and in the MessageBoard-Interface.
	 * This method  
	 * @param msg
	 */
	public void publish(Message msg) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * registerServer is capsuled in the ParentServer-Interface. This method is called from a child-server to bind it's
	 * self to it's parent. The register is important for the notify process
	 * @param childServer
	 * @throws RemoteException
	 */
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

	/**
	 * registerClient is capsuled in the MessageBoard-Interface .
	 * This method can be called by a client to connect his self by his group server.
	 * @param client
	 * @param username
	 * @param password
	 * @return
	 * @throws RemoteException
	 */
	public UUID registerClient(Notifiable client, String username, String password) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * notifyNew is capsuled in the Notifyable-Interface. Every time a new message is transmitted to the server
	 * either trough a client or the parent, the notifyNew-method notifies all components which implements the
	 * Notify-Interface.
	 * @param msg
	 * @throws RemoteException
	 */
	public void notifyNew(Message msg) throws RemoteException {
		
	}

	/**
	 * notifyDelete is capsuled in the Notifyable-Interface. Every time a message will be deleted on the server
	 * trough a admin, the notifyNew-method notifies all components which implements the
	 * Notify-Interface.
	 * @param msg
	 * @throws RemoteException
	 */
	public void notifyDelete(Message msg) throws RemoteException {

	}


	/**
	 * notifyEdit is capsuled in the Notifyable-Interface. Every time a message will be edited on the server
	 * trough a admin or client, the notifyNew-method notifies all components which implements the
	 * Notify-Interface.
	 * @param msg
	 * @throws RemoteException
	 */
	public void notifyEdit(Message msg) throws RemoteException {

	}

	/**
	 * addParent will only be called by the server.class to bind the parent to this server.
	 * @param parent
	 */
	protected void addParent(ParentServer parent){
			this.parent = parent;
	}
}
