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
import de.htwsaar.wirth.server.service.Services;
import de.htwsaar.wirth.server.util.CommandRunner;
import de.htwsaar.wirth.server.util.commandFactory.CommandBuilder;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Command;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Constants.ChildCmd;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Constants.Cmd;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Constants.ParentCmd;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.childCommand.ChildCommand;


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

	/**
	 * checks, if the current Server is the root-server
	 * @return
	 */
	private boolean isRoot() {
		return this.parent == null;
	}
	
	/**
	 * notify all clients
	 * @param handler
	 */
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

	/**
	 * queue up a command for each child-server
	 * @param cmd
	 */
	private void queueCommandForAllChildServer(ChildCommand cmd) {
		synchronized (childServerList) {
			for (Notifiable childServer : childServerList) {
				ChildCommand clonedCommand = cmd.clone();
				clonedCommand.setNotifiable(childServer);
				childServerQueueMap.get(childServer).addCommand(clonedCommand);
			}
		}
	}
	
	//---------------------------------- MessageBoard Interface ----------------------------------
	
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
	 * @param newUsername
	 * @param newPassword
	 * @param username
	 * @param token
	 */
	public void addUser(String newUsername, String newPassword, String username, UUID token) throws RemoteException {
		SessionManager.isAuthenticatedByToken(username, token);
		// TODO:
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
		SessionManager.isAuthenticatedByToken(username, token);
		// TODO:
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
		SessionManager.isAuthenticatedByToken(username, token);
		Message message = new MessageImpl(msg, username, groupName, false);
		notifyNew(message);
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
		SessionManager.isAuthenticatedByToken(username, token);
		//TODO:
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
		SessionManager.isAuthenticatedByToken(username, token);
		//TODO:
	}
	
	/**
	 * getMessages is used by the MessageBoard-Interface.
	 * This method can be called by a client to
	 * initialise and update his messageboard
	 *
	 * @param username
	 * @param token
	 * @return
	 * @throws RemoteException
	 */
	public List<Message> getMessages(String username, UUID token) throws RemoteException {
		SessionManager.isAuthenticatedByToken(username, token);
		return getMessages();
	}
	
	//---------------------------------- Notifiable Interface ----------------------------------
	
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
		//TODO: Save msg to local database

		// Add a NewMessageCommand to each CommandRunner
		queueCommandForAllChildServer(CommandBuilder.buildChildCommand(msg, ChildCmd.NEW));

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
		//TODO: Edit msg to local database

		// only execute the following, if the edit was successful

		// Add a EditMessageCommand to each CommandRunner
		queueCommandForAllChildServer(CommandBuilder.buildCommand(msg,Cmd.EDIT));

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
		//TODO: Delete msg from local database

		// only execute the following, if the delete was successful

		// Add a DeleteMessageCommand to each CommandRunner
		queueCommandForAllChildServer(CommandBuilder.buildCommand(msg,Cmd.DELETE));

		// Notify each client
		notifyClients((cl) -> cl.notifyDelete(msg));
	}
	
	//---------------------------------- ParentServer Interface ----------------------------------
	
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
	 * publish is capsuled in the ParentServer-Interface and in the
	 * MessageBoard-Interface. This method
	 *
	 * @param msg
	 */
	public void publish(Message msg) throws RemoteException {
		//TODO: check if we are the root server

		// Change msg to a published msg in the local database

		// Add a PublishMessageCommand to the ParentQueue
		Command cmd = CommandBuilder.buildParentCommand(parent, msg, ParentCmd.PUBLISH);
		childServerQueueMap.get(parent).addCommand(cmd);

		// Notify each client
		notifyClients((cl) -> cl.notifyNew(msg));
	}
	
	public void notifyServerEdit(Message msg) throws RemoteException {
		// TODO
	}

	public void notifyServerDelete(Message msg) throws RemoteException {
		// TODO
	}

	public List<Message> getMessages() throws RemoteException {
		return Services.getInstance().getMessageService().getAll();
	}
}
