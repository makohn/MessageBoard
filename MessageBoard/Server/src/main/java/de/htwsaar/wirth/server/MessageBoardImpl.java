package de.htwsaar.wirth.server;

import de.htwsaar.wirth.remote.MessageBoard;
import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.MessageImpl;
import de.htwsaar.wirth.remote.model.auth.AuthPacket;
import de.htwsaar.wirth.remote.model.auth.LoginPacket;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.service.Services;
import de.htwsaar.wirth.server.util.CommandRunner;
import de.htwsaar.wirth.server.util.command.ChildCmd;
import de.htwsaar.wirth.server.util.command.Command;
import de.htwsaar.wirth.server.util.command.CommandBuilder;
import de.htwsaar.wirth.server.util.command.ParentCmd;
import de.htwsaar.wirth.server.util.command.child.ChildCommand;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


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

	/**
	 *	needToSend decides whether a message has to send to the parent or not.
	 */
	private boolean needToSendParent(Message msg){
		// add condition Database has msg
		return (msg.isPublished() && !isRoot());
	}

	/**
	 *	needToPublish decides whether a message has to publish to the parent or not.
	 */
	private boolean needToPublish(Message msg){
		// add condition Database has msg
		return (!msg.isPublished() && !isRoot());
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
	public AuthPacket registerClient(LoginPacket login, Notifiable client) throws RemoteException {
		// Authenticate throws an exception, if the username or password are
		// wrong
		// this exception can be handled on clientside
		AuthPacket auth = SessionManager.authenticate(login);
		clientList.add(client);
		return auth;
	}
	
	/**
	 * @param auth
	 * @param newUsername
	 * @param newPassword
	 */
	public void addUser(AuthPacket auth, String newUsername, String newPassword) throws RemoteException {
		SessionManager.isAuthenticatedByToken(auth);
		SessionManager.isGroupLeader(auth);
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
	public void publish(AuthPacket auth, Message msg) throws RemoteException {
		SessionManager.isAuthenticatedByToken(auth);
		SessionManager.isGroupLeader(auth);

		if(needToPublish(msg)) {
			msg.setPublished(true);
			Command cmd = CommandBuilder.buildParentCommand(parent,msg,ParentCmd.PUBLISH);
			parentQueue.addCommand(cmd);
		}



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
	public void newMessage(AuthPacket auth, String msg) throws RemoteException {
		SessionManager.isAuthenticatedByToken(auth);
		Message message = new MessageImpl(msg, auth.getUsername(), groupName, false);
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
	public void editMessage(AuthPacket auth, Message msg) throws RemoteException {
		SessionManager.isAuthenticatedByToken(auth);
		SessionManager.isAuthor(auth, msg);
		notifyEdit(msg);
		if(needToSendParent(msg)) {
			Command cmd = CommandBuilder.buildParentCommand(parent, msg, ParentCmd.EDIT);
			parentQueue.addCommand(cmd);
		}
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
	public void deleteMessage(AuthPacket auth, Message msg) throws RemoteException {
		SessionManager.isAuthenticatedByToken(auth);
		SessionManager.isGroupLeader(auth);
		notifyDelete(msg);
		if(needToSendParent(msg)) {
			Command cmd = CommandBuilder.buildParentCommand(parent, msg, ParentCmd.DELETE);
			parentQueue.addCommand(cmd);
		}
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
	public List<Message> getMessages(AuthPacket auth) throws RemoteException {
		SessionManager.isAuthenticatedByToken(auth);
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
		//Services.getInstance().getMessageService().saveMessage(msg);

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
		//TODO: check if the edit msg, we get notified of, is newer than the one we have in the database
		//Services.getInstance().getMessageService().saveMessage(msg);
		//TODO: Edit msg to local database (only when Element in DB then successful)

		// only execute the following, if the edit was successful

		// Add a EditMessageCommand to each CommandRunner
		queueCommandForAllChildServer(CommandBuilder.buildChildCommand(msg,ChildCmd.EDIT));

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
		//Services.getInstance().getMessageService().deleteMessage(msg);
		//TODO: Delete msg from local database (only when Element in DB then successful)

		// only execute the following, if the delete was successful

		// Add a DeleteMessageCommand to each CommandRunner
		queueCommandForAllChildServer(CommandBuilder.buildChildCommand(msg,ChildCmd.DELETE));

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
		notifyNew(msg);
	}


	public void notifyServerEdit(Message msg) throws RemoteException {
		notifyEdit(msg);
		if(needToSendParent(msg)) {
			Command cmd = CommandBuilder.buildParentCommand(parent, msg, ParentCmd.EDIT);
			parentQueue.addCommand(cmd);
			notifyServerEdit(msg);
		}


	}

	public void notifyServerDelete(Message msg) throws RemoteException {
		notifyDelete(msg);
		if(needToSendParent(msg)) {
			Command cmd = CommandBuilder.buildParentCommand(parent, msg, ParentCmd.DELETE);
			parentQueue.addCommand(cmd);
			notifyServerEdit(msg);
		}
	}

	public List<Message> getMessages() throws RemoteException {
		return Services.getInstance().getMessageService().getAll();
	}
}
