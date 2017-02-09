package de.htwsaar.wirth.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import de.htwsaar.wirth.remote.MessageBoard;
import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.MessageImpl;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.Command;
import de.htwsaar.wirth.server.util.CommandRunner;
import de.htwsaar.wirth.server.util.DeleteMessageCommand;
import de.htwsaar.wirth.server.util.EditMessageCommand;
import de.htwsaar.wirth.server.util.NewMessageCommand;
import de.htwsaar.wirth.server.util.PublishMessageCommand;

public class MessageBoardImpl extends UnicastRemoteObject implements Notifiable, MessageBoard, ParentServer {

	private String groupName;

	/**
	 * childServerList with each ChildServer
	 */
	private List<Notifiable> childServerList;

	/**
	 * a Map with CommandRunners used to lookup a CommandRunner by its
	 * Notifiable each CommandRunner is a Thread, which executes every Command
	 * it gets
	 */
	private Map<Notifiable, CommandRunner> childServerQueueMap;

	/**
	 * the parent of this server null, if this server is the root-server
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

	private static final long serialVersionUID = -4613549994529764225L;

	public MessageBoardImpl(String groupName) throws RemoteException {
		this.groupName = groupName;
		childServerList = Collections.synchronizedList(new ArrayList<Notifiable>());
		childServerQueueMap = new ConcurrentHashMap<Notifiable, CommandRunner>();

		clientList = Collections.synchronizedList(new ArrayList<Notifiable>());

	}

	private boolean isRoot() {
		return this.parent == null;
	}

	private void notifyClients(ClientNotifyHandler handler) {
		synchronized (clientList) {
			for (Notifiable client : clientList) {
				try {
					handler.handle(client);
				} catch (RemoteException e) {
					// Ignore it, if a client cannot be called back
					e.printStackTrace();
				}
			}
		}
	}
	
	private void queueCommandForAllChildServer(Command cmd) {
		synchronized (childServerList) {
			for (Notifiable childServer : childServerList) {
				Command clonedCommand = cmd.clone();
				clonedCommand.setNotifiable(childServer);
				childServerQueueMap.get(childServer).addCommand(clonedCommand);
			}
		}
	}

	public void notifyServerDelete(Message msg) throws RemoteException {
		// TODO Auto-generated method stub

	}

	public void notifyServerEdit(Message msg) throws RemoteException {
		// TODO Auto-generated method stub
	}

	/**
	 * publish is capsuled in the ParentServer-Interface and in the
	 * MessageBoard-Interface. This method
	 * 
	 * @param msg
	 */
	public void publish(Message msg) throws RemoteException {
		// check if we are the root server

		// Change msg to a published msg in the local database

		// Add a PublishMessageCommand to the ParentQueue
		PublishMessageCommand cmd = new PublishMessageCommand(parent, msg);
		childServerQueueMap.get(parent).addCommand(cmd);

		// Notify each client
		notifyClients((cl) -> cl.notifyNew(msg));
	}

	/**
	 * registerServer is capsuled in the ParentServer-Interface. This method is
	 * called from a child-server to bind itself to its parent. The register is
	 * important for the notify process
	 * 
	 * @param childServer
	 * @throws RemoteException
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
	 * @param msg
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
		// Authenticate throws an exception, if the username or password are
		// wrong
		// this exception can be handled on clientside
		UUID userToken = SessionManager.authenticate(username, password);
		clientList.add(client);
		return userToken;
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
		queueCommandForAllChildServer(new NewMessageCommand(msg));

		// Notify each client
		notifyClients((cl) -> cl.notifyNew(msg));
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
		queueCommandForAllChildServer(new EditMessageCommand(msg));
		
		// Notify each client
		notifyClients((cl) -> cl.notifyEdit(msg));
	}

	/**
	 * notifyDelete is capsuled in the Notifyable-Interface. Every time a
	 * message will be deleted on the server through a admin, the
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
		queueCommandForAllChildServer(new DeleteMessageCommand(msg));

		// Notify each client
		notifyClients((cl) -> cl.notifyDelete(msg));
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
