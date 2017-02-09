package de.htwsaar.wirth.server;

import de.htwsaar.wirth.remote.MessageBoard;
import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.MessageImpl;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.CommandRunner;
import de.htwsaar.wirth.server.util.commandFactory.CommandBuilder;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Command;
import de.htwsaar.wirth.server.util.commandFactory.factories.interfaces.ChildCommandFactory;
import de.htwsaar.wirth.server.util.commandFactory.factories.interfaces.CommandFactory;
import de.htwsaar.wirth.server.util.commandFactory.factories.interfaces.ParentCommandFactory;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MessageBoardImpl extends UnicastRemoteObject implements Notifiable, MessageBoard, ParentServer {

	private String groupName;

	
	/**
	 *  childServerList with each ChildServer
	 */
	private List<Notifiable> childServerList;
	
	/**
	 *  a Map with CommandRunners used to lookup a CommandRunner by its Notifiable
	 *  each CommandRunner is a Thread, which executes every Command it gets
	 */
	private Map<Notifiable, CommandRunner> childServerQueueMap;

	/**
	 * the parent of this server
	 * null, if this server is the root-server
	 */
	private ParentServer parent;
	
	/**
	 * the queue to send commands to the parent
	 */
	private CommandRunner parentQueue;
	
	/**
	 * a list of clients
	 */
	private List<Notifiable> clientList;

	/**
	 * the session map, which stores a authentification token for each username
	 */
	private Map<String, UUID> sessions;
	
	private static final long serialVersionUID = -4613549994529764225L;

	public MessageBoardImpl(String groupName) throws RemoteException {
		this.groupName = groupName;
		childServerList = Collections.synchronizedList(new ArrayList<Notifiable>());
		childServerQueueMap = new ConcurrentHashMap<Notifiable, CommandRunner>();

		clientList = Collections.synchronizedList(new ArrayList<Notifiable>());

		sessions = new ConcurrentHashMap<String, UUID>();

	}
	
	private boolean isRoot() {
		return this.parent == null;
	}

	public void notifyServerDelete(Message msg) throws RemoteException {
		// TODO Auto-generated method stub

	}

	public void notifyServerEdit(Message msg) throws RemoteException {
		// TODO Auto-generated method stub
	}

	/**
	 * publish is capsuled in the ParentServer-Interface.
	 * 
	 * @param msg
	 */
	public void publish(Message msg) throws RemoteException {
		// check if we are the root server
		
		// Change msg to a published msg in the local database

		// Add a PublishMessageCommand to the ParentQueue
		Command cmd = CommandBuilder.buildParentCommand(parent,msg, ParentCommandFactory.PUBLISH_COMMAND);
		childServerQueueMap.get(parent).addCommand(cmd);

		// Notify each client
		for (Notifiable client : clientList) {
			try {
				client.notifyNew(msg);
			} catch (RemoteException e) {
				// Ignore it, if a client cannot be called back
				e.printStackTrace();
			}
		}
	}

	/**
	 * registerServer is capsuled in the ParentServer-Interface. This method is
	 * called from a child-server to bind itself to its parent. The register is
	 * important for the notify process
	 * 
	 * @param childServer a server which will be notified by his parent
	 * @throws RemoteException An Exception which occurs if the server is not responding
	 */
	public void registerServer(Notifiable childServer) throws RemoteException {
		childServerList.add(childServer);
		CommandRunner commandRunner = new CommandRunner();
		commandRunner.start();
		childServerQueueMap.put(childServer, commandRunner);
	}

	/**
	 * NewMessage is capsuled in the MessageBoard-Interface. This method can be
	 * called by the clients to submit a new message to their group-server. To
	 * authenticate the User a username and a valid authentication token is
	 * required.
	 * 
	 * @param msg c
	 * @param username
	 * @param token
	 * @throws RemoteException
	 */
	public void newMessage(String msg, String username, UUID token) throws RemoteException {
		Message message = new MessageImpl(msg, username, groupName, false);
		notifyNew(message);
	}

	/**
	 * deleteMessage is capsuled in the MessageBoard-Interface. This method can
	 * be called by the admin to delete a existing message on their
	 * group-server. To authenticate the User a username and a valid
	 * authentication token is required.
	 * 
	 * @param msg
	 * @param username
	 * @param token
	 * @throws RemoteException
	 */
	public void deleteMessage(Message msg, String username, UUID token) throws RemoteException {

	}

	/**
	 * editMessage is capsuled in the MessageBoard-Interface. This method can be
	 * called by the author of a message to edit the existing message on their
	 * group-server. In case the message was published the change effects also
	 * all other servers which received this published message. To authenticate
	 * the User a username and a valid authentication token is required.
	 * 
	 * @param msg
	 * @param username
	 * @param token
	 * @throws RemoteException
	 */
	public void editMessage(Message msg, String username, UUID token) throws RemoteException {

	}

	/**
	 * publish is capsuled in the MessageBoard-Interface. This method can be
	 * called by the admin to push a existing message to the parent-server. To
	 * authenticate the User a username and a valid authentication token is
	 * required.
	 * 
	 * @param msg
	 * @param username
	 * @param token
	 * @throws RemoteException
	 */
	public void publish(Message msg, String username, UUID token) throws RemoteException {

	}

	/**
	 * getMessages is capsuled in the MessageBoard-Interface and in the
	 * ParentServer-Interface. This method can be called by a client to
	 * initialise his messageboard or it can called by a just new born server
	 * which want to receive all messages from his parent .
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public List<Message> getMessages() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * registerClient is capsuled in the MessageBoard-Interface . This method
	 * can be called by a client to connect his self by his group server.
	 * 
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
	 * notifyNew is capsuled in the Notifyable-Interface. Every time a new
	 * message is transmitted to the server either trough a client or the
	 * parent, the notifyNew-method notifies all components which implements the
	 * Notify-Interface.
	 * 
	 * @param msg
	 * @throws RemoteException
	 */
	public void notifyNew(Message msg) throws RemoteException {
		// Save msg to local database

		// Add a NewMessageCommand to each CommandRunner
		for (Notifiable childServer : childServerList) {
			Command cmd = CommandBuilder.buildChildCommand(childServer,msg, ChildCommandFactory.NEW_COMMAND);
			childServerQueueMap.get(childServer).addCommand(cmd);
		}

		// Notify each client
		for (Notifiable client : clientList) {
			try {
				client.notifyNew(msg);
			} catch (RemoteException e) {
				// Ignore it, if a client cannot be called back
				e.printStackTrace();
			}
		}
	}

	/**
	 * notifyDelete is capsuled in the Notifyable-Interface. Every time a
	 * message will be deleted on the server trough a admin, the
	 * notifyNew-method notifies all components which implements the
	 * Notify-Interface.
	 * 
	 * @param msg
	 * @throws RemoteException
	 */
	public void notifyDelete(Message msg) throws RemoteException {
		// Delete msg from local database

		// only execute the following, if the delete was successful

		// Add a DeleteMessageCommand to each CommandRunner
		for (Notifiable childServer : childServerList) {
			Command cmd = CommandBuilder.buildCommand(childServer,msg,CommandFactory.DELETE_COMMAND);
			childServerQueueMap.get(childServer).addCommand(cmd);
		}

		// Notify each client
		for (Notifiable client : clientList) {
			try {
				client.notifyDelete(msg);
			} catch (RemoteException e) {
				// Ignore it, if a client cannot be called back
				e.printStackTrace();
			}
		}
	}

	/**
	 * notifyEdit is capsuled in the Notifyable-Interface. Every time a message
	 * will be edited on the server trough a admin or client, the
	 * notifyNew-method notifies all components which implements the
	 * Notify-Interface.
	 * 
	 * @param msg
	 * @throws RemoteException
	 */
	public void notifyEdit(Message msg) throws RemoteException {
		// Edit msg to local database

		// only execute the following, if the edit was successful

		// Add a EditMessageCommand to each CommandRunner
		for (Notifiable childServer : childServerList) {
			Command cmd = CommandBuilder.buildCommand(childServer,msg, CommandFactory.EDIT_COMMAND);
			childServerQueueMap.get(childServer).addCommand(cmd);
		}

		// Notify each client
		for (Notifiable client : clientList) {
			try {
				client.notifyEdit(msg);
			} catch (RemoteException e) {
				// Ignore it, if a client cannot be called back
				e.printStackTrace();
			}
		}
	}

	/**
	 * addParent will only be called by the server.class to bind the parent to
	 * this server.
	 * 
	 * @param parent
	 */
	public void setParent(ParentServer parent) {
		if (parentQueue != null) {
			parentQueue.interrupt();
		}
		this.parent = parent;
		this.parentQueue = new CommandRunner();
	}
}
