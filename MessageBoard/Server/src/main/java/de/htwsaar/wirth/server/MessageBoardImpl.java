package de.htwsaar.wirth.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.htwsaar.wirth.remote.MessageBoard;
import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.NotifiableClient;
import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.exceptions.MessageNotExistsException;
import de.htwsaar.wirth.remote.exceptions.NoPermissionException;
import de.htwsaar.wirth.remote.exceptions.UserAlreadyExistsException;
import de.htwsaar.wirth.remote.exceptions.UserNotExistsException;
import de.htwsaar.wirth.remote.model.MessageImpl;
import de.htwsaar.wirth.remote.model.Status;
import de.htwsaar.wirth.remote.model.UserImpl;
import de.htwsaar.wirth.remote.model.auth.AuthPacket;
import de.htwsaar.wirth.remote.model.auth.LoginPacket;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.remote.model.interfaces.User;
import de.htwsaar.wirth.remote.util.HashUtil;
import de.htwsaar.wirth.server.service.Services;
import de.htwsaar.wirth.server.service.interfaces.MessageService;
import de.htwsaar.wirth.server.util.CommandRunner;
import de.htwsaar.wirth.server.util.command.ChildCmd;
import de.htwsaar.wirth.server.util.command.Command;
import de.htwsaar.wirth.server.util.command.CommandBuilder;
import de.htwsaar.wirth.server.util.command.ParentCmd;
import de.htwsaar.wirth.server.util.command.child.ChildCommand;


public class MessageBoardImpl /*extends UnicastRemoteObject*/ implements Notifiable, MessageBoard, ParentServer {
	
	/**
	 * the sessionManager for the MessageBoard
	 */
	private SessionManager sessionManager;

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
	private Map<String, NotifiableClient> clientNotifyMap;
	
	/**
	 * a map to manage the status of each user
	 */
	private Map<String, Status> userStatus;

	/**
	 * a ThreadPool we use for callbacks to clients
	 */
	private ExecutorService threadPool;

	public MessageBoardImpl(String groupName, int localPort, boolean isRoot) throws RemoteException {

		sessionManager = new SessionManager(groupName, isRoot);
		
		childServerList = Collections.synchronizedList(new ArrayList<Notifiable>());
		childServerQueueMap = new ConcurrentHashMap<Notifiable, CommandRunner>();
		
		clientNotifyMap = new ConcurrentHashMap<String, NotifiableClient>();
		userStatus = new ConcurrentHashMap<String, Status>();
		
		// set the default userstatus of each user to offline
		for (User user : Services.getInstance().getUserService().getAll()) {
			userStatus.put(user.getUsername(), Status.OFFLINE);
		}
		threadPool = Executors.newCachedThreadPool();
		
		UnicastRemoteObject.exportObject(this, localPort);
	}
	

	/**
	 * addParent will only be called by the server.class to bind the parent to
	 * this server.
	 *
	 * @param parent
	 * @throws RemoteException 
	 */
	public void setParent(ParentServer parent) throws RemoteException {
		if (parentQueue != null) {
			parentQueue.interrupt();
		}
		this.parent = parent;
		this.parentQueue = new CommandRunner();
		parentQueue.start();
		
		// müsste eigentlich nur beim ersten Start durchgeführt werden
		syncParent();
	}

	private void syncParent() throws RemoteException {
		List<Message> parentMessages = parent.getMessages();
		List<Message> childMessages = Services.getInstance().getMessageService().getAll();
		
		// create a map from the childMsgs
		Map<UUID, Message> map = new HashMap<>();
		for(Message childMsg : childMessages) {
			map.put(childMsg.getID(), childMsg);
		}
		
		// iterate over the parentMessages and check, if the parentMsg is newer than the childMsg
		for(Message parentMsg : parentMessages) {
			Message childMsg = map.get(parentMsg.getID());
			if (childMsg != null) {
				if(parentMsg.getModifiedAt().after(childMsg.getModifiedAt())) {
					// save the parentMsg, if it is newer
					Services.getInstance().getMessageService().saveMessage(parentMsg);
				} else if (childMsg.getModifiedAt().after(parentMsg.getModifiedAt())) {
					// otherwise we need to notify the parent, that we have a newer message
					Command cmd = CommandBuilder.buildParentCommand(parent, childMsg, ParentCmd.EDIT);
					parentQueue.addCommand(cmd);
				}
			} else {
				// if we don't have a message the parent has, we need to save it
				Services.getInstance().getMessageService().saveMessage(parentMsg);
			}
		}
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
		for (Entry<String, NotifiableClient> entry : clientNotifyMap.entrySet()) {
				threadPool.execute(() -> {
					try {
						handler.handle(entry.getValue());
					} catch (RemoteException e) {
						e.printStackTrace();
						// if we catch a remoteException the callback for this client doesn't work
						if (clientNotifyMap.containsValue(entry.getValue())) {
							clientNotifyMap.remove(entry);
							//changeUserStatusAndNotifyClients(entry.getKey(), Status.SHOW_AS_OFFLINE);
						}
					}
				});
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
	
	private void changeUserStatusAndNotifyClients(String username, Status status) {
		userStatus.put(username, status);
		notifyClients(cl -> cl.notifyUserStatus(username, status));
	}

	/**
	 *	needToSend decides whether a message has to send to the parent or not.
	 */
	private boolean needToSendParent(Message msg){
		// FIXME: add condition Database has msg
		// wäre es nicht besser einfach grundsätzlich hochzuschieben und wenn der parent die nachricht nicht hat ignoriert er es
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
	 * @return
	 * @throws RemoteException
	 */
	public AuthPacket login(LoginPacket login, NotifiableClient client) throws RemoteException {
		// Authenticate throws an exception, if the username or password are wrong
		// this exception can be handled on clientside
		AuthPacket auth = sessionManager.authenticate(login);
		changeUserStatusAndNotifyClients(login.getUsername(), Status.ONLINE);
		// add the Notifiable of the client to the clientNotifyMap
		clientNotifyMap.put(login.getUsername(), client);
		return auth;
	}
	
	public void logout(AuthPacket auth) throws RemoteException {
		sessionManager.verifyAuthPacket(auth);
		// remove the user from the clientNotifyMap and set his status to offline
		clientNotifyMap.remove(auth.getUsername());
		sessionManager.logout(auth.getUsername());
		
		changeUserStatusAndNotifyClients(auth.getUsername(), Status.OFFLINE);
	}
	
	public void changeUserStatus(AuthPacket auth, Status status) throws RemoteException {
		sessionManager.verifyAuthPacket(auth);
		String username = auth.getUsername();
		changeUserStatusAndNotifyClients(username, status);
	}
		
	/**
	 * @param auth
	 * @param newUsername
	 * @param newPassword
	 * @throws RemoteException
	 * @throws UserAlreadyExistsException, if the username is already in use
	 */
	public void addUser(AuthPacket auth, String newUsername, String newPassword) throws RemoteException{
		sessionManager.verifyAuthPacket(auth);
		if (!sessionManager.isGroupLeader(auth)) {
			throw new NoPermissionException("The user is not a group-leader");
	    }
		
		synchronized (User.class) {
			if (Services.getInstance().getUserService().getUser(newUsername) == null) {
				// hash the password
				newPassword = HashUtil.hashSha512(newPassword);
				
				// create a new user object and save it in the database
				User user = new UserImpl(newUsername, "", "", newPassword, false);
				Services.getInstance().getUserService().saveUser(user);
				
				// notify the clients of the new offline user
				changeUserStatusAndNotifyClients(newUsername, Status.OFFLINE);
			} else {
				throw new UserAlreadyExistsException();
			}
		}
	}
	
	public void deleteUser(AuthPacket auth, String username) throws RemoteException {
		sessionManager.verifyAuthPacket(auth);
		if (!sessionManager.isGroupLeader(auth)) {
			throw new NoPermissionException("The user is not a group-leader");
		}
		synchronized (User.class) {
			// check if the user exists
			User user = Services.getInstance().getUserService().getUser(username);
			if ( user == null ) {
				throw new UserNotExistsException("This User does not exist on this server");
			}
			
			Services.getInstance().getUserService().deleteUser(user);
		}
		sessionManager.logout(username);
		clientNotifyMap.remove(username);
		
		notifyClients(cl -> cl.notifyDeleteUser(username));
		
		userStatus.remove(username);		
	}
	
	/**
	 * publish is capsuled in the MessageBoard-Interface. This method can be
	 * called by the admin to push a existing message to the parent-server. To
	 * authenticate the User a username and a valid authentication token is
	 * required.
	 *
	 * @param auth AuthPack given by the login
	 * @param msg
	 * @throws RemoteException
	 */
	public void publish(AuthPacket auth, UUID id) throws RemoteException {
		sessionManager.verifyAuthPacket(auth);
		if (!sessionManager.isGroupLeader(auth)) {
			throw new NoPermissionException("The user is not a group-leader");
		}
		Message msg = Services.getInstance().getMessageService().getMessage(id);
		if ( msg == null ) {
			throw new MessageNotExistsException("The message doesn't exists on this server");
		}
		if(needToPublish(msg)) {
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
	 * @param auth AuthPack given by the login
	 * @param msg
	 * @throws RemoteException
	 */
	public void newMessage(AuthPacket auth, String msg) throws RemoteException {
		sessionManager.verifyAuthPacket(auth);
		Message message = new MessageImpl(msg, auth.getUsername(), sessionManager.getGroupName());
		notifyNew(message);
	}
	
	/**
	 * editMessage is capsuled in the MessageBoard-Interface. This method can be
	 * called by the author of a message to edit the existing message on their
	 * group-server. In case the message was published the change effects also
	 * all other servers which received this published message. To authenticate
	 * the User a username and a valid authentication token is required.
	 *
	 * @param auth AuthPack given by the login
	 * @param msgTxt
	 * @throws RemoteException
	 */
	public void editMessage(AuthPacket auth, String msgTxt, UUID id) throws RemoteException {
		sessionManager.verifyAuthPacket(auth);
		Message oldMsg;
		synchronized (Message.class) {
			oldMsg = Services.getInstance().getMessageService().getMessage(id);
			if ( oldMsg == null)
				throw new MessageNotExistsException("The message doesn't exists on this server");
			if (!sessionManager.isAuthor(auth, oldMsg)) {
	            throw new NoPermissionException("The user is not the author"); 
			}
			oldMsg.changeMessage(msgTxt);
			notifyEdit(oldMsg);
		}
		if(needToSendParent(oldMsg)) {
			Command cmd = CommandBuilder.buildParentCommand(parent, oldMsg, ParentCmd.EDIT);
			parentQueue.addCommand(cmd);
		}
	}	
	
	/**
	 * deleteMessage is capsuled in the MessageBoard-Interface. This method can
	 * be called by the admin to delete a existing message on their
	 * group-server. To authenticate the User a username and a valid
	 * authentication token is required.
	 *
	 * @param auth AuthPack given by the login
	 * @param id
	 * @throws RemoteException
	 */
	public void deleteMessage(AuthPacket auth, UUID id) throws RemoteException {
		sessionManager.verifyAuthPacket(auth);
		Message msgToDelete;
		synchronized (Message.class) {
			msgToDelete = Services.getInstance().getMessageService().getMessage(id);
			if ( Services.getInstance().getMessageService().getMessage(id) == null ) {
				throw new MessageNotExistsException("The message doesn't exists on this server");
			}
			if (!sessionManager.isAuthor(auth, msgToDelete) && !sessionManager.isGroupLeader(auth)) {
				throw new NoPermissionException("The user is neither the author nor a groupleader");
			}
			notifyDelete(msgToDelete);
		}
		if(!isRoot()) {
			Command cmd = CommandBuilder.buildParentCommand(parent, msgToDelete, ParentCmd.DELETE);
			parentQueue.addCommand(cmd);
		}
	}
	
	/**
	 * getMessages is used by the MessageBoard-Interface.
	 * This method can be called by a client to
	 * initialise and update his messageboard
	 *
	 * @param auth AuthPack given by the login
	 * @return
	 * @throws RemoteException
	 */
	public List<Message> getMessages(AuthPacket auth) throws RemoteException {
		sessionManager.verifyAuthPacket(auth);
		return getMessages();
	}
		
	public Map<String, Status> getUserStatus(AuthPacket auth) throws RemoteException {
		sessionManager.verifyAuthPacket(auth);
		return userStatus;
	}
	
	//---------------------------------- Notifiable Interface ----------------------------------


	/**
	 * notifyNew is capsuled in the Notifyable-Interface. Every time a new
	 * message is transmitted to the server either trough a client or the
	 * parent, the notifyNew-method notifies all components which implements the
	 * Notify-Interface.
	 *
	 * @param msg message to be created
	 * @throws RemoteException
	 */
	public void notifyNew(Message msg) throws RemoteException {
		Message clonedMessage = new MessageImpl(msg);
		Services.getInstance().getMessageService().saveMessage(msg);
		clonedMessage.setPublished(true);

		// Add a NewMessageCommand to each CommandRunner
		queueCommandForAllChildServer(CommandBuilder.buildChildCommand(clonedMessage, ChildCmd.NEW));

		// Notify each client
		notifyClients(cl -> cl.notifyNew(msg));
	}

	/**
	 * notifyEdit is capsuled in the Notifyable-Interface. Every time a message
	 * will be edited on the server through a admin or client, the
	 * notifyNew-method notifies all components which implements the
	 * Notify-Interface.
	 *
	 * @param msg message to be edited
	 * @throws RemoteException
	 */		
	public void notifyEdit(Message msg) throws RemoteException {
		Message clonedMessage = new MessageImpl(msg);
		
		synchronized(Message.class) {
			MessageService msgService = Services.getInstance().getMessageService();
			boolean isPublishedStatus = msgService.getMessage(msg.getID()).isPublished();
			msg.setPublished(isPublishedStatus);
			msgService.saveMessage(msg/*message*/);
		}
		
		// Add a EditMessageCommand to each CommandRunner
		queueCommandForAllChildServer(CommandBuilder.buildChildCommand(clonedMessage, ChildCmd.EDIT));

		// Notify each client
		notifyClients(cl -> cl.notifyEdit(msg));
	}

	/**
	 * notifyDelete is capsuled in the Notifyable-Interface. Every time a
	 * message will be deleted on the server through a admin, the
	 * notifyNew-method notifies all components which implements the
	 * Notify-Interface.
	 *
	 * @param msg message to be delete
	 * @throws RemoteException
	 */
	public void notifyDelete(Message msg) throws RemoteException {
		Services.getInstance().getMessageService().deleteMessage(msg);
		// Add a DeleteMessageCommand to each CommandRunner
		queueCommandForAllChildServer(CommandBuilder.buildChildCommand(msg, ChildCmd.DELETE));
		// Notify each client
		notifyClients(cl -> cl.notifyDelete(msg));
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
	 * notifyServerDelete is capsuled in the ParentServer-Interface. Only if the message exist in
	 * the Database this Method will be published and send a newMessage-command to all child-server.
	 * In addition it will notify all clients.
	 * If the message was already published this method has no effect.
	 * @param msg
	 * @throws RemoteException
	 */
	public void publish(Message msg) throws RemoteException {
		Services.getInstance().getMessageService().saveMessage(msg);
		
		Message clonedMessage = new MessageImpl(msg);
		// Notify each client
		notifyClients(cl -> cl.notifyNew(clonedMessage));
		
		msg.setPublished(true);
		// Add a NewMessageCommand to each CommandRunner
		queueCommandForAllChildServer(CommandBuilder.buildChildCommand(msg, ChildCmd.NEW));
	}


	/**
	 * notifyServerEdit is capsuled in the ParentServer-Interface. Only if the message exist in
	 * the Database this Method will edit the entry and send a edit-command to all child-server.
	 * In addition it will notify all clients.
	 * If the message was published, the edit-command will be send further to the parent.
	 * @param msg
	 * @throws RemoteException
	 */
	public void notifyServerEdit(Message msg) throws RemoteException {
		notifyEdit(msg);
		if(needToSendParent(msg)) {
			Command cmd = CommandBuilder.buildParentCommand(parent, msg, ParentCmd.EDIT);
			parentQueue.addCommand(cmd);
		}
	}

	/**
	 * notifyServerDelete is capsuled in the ParentServer-Interface. Only if the message exist in
	 * the Database this Method will delete the entry and send a delete-command to all child-server.
	 * In addition it will notify all clients.
	 * If the message was published, the delete-command will be send further to the parent.
	 * @param msg
	 * @throws RemoteException
	 */
	public void notifyServerDelete(Message msg) throws RemoteException {
		synchronized (Message.class) {
			boolean hasMsg = Services.getInstance().getMessageService().getMessage(msg.getID()) != null;
			if (hasMsg) {
				notifyDelete(msg);		
				if(!isRoot()) {
					Command cmd = CommandBuilder.buildParentCommand(parent, msg, ParentCmd.DELETE);
					parentQueue.addCommand(cmd);
				}
			}
		}
	}

	public List<Message> getMessages() throws RemoteException {
		// hier evtl noch ein limit fuer nachrichten
		// TODO: wir sollten jeweils methoden mit limitierung und filterung nach gruppe erstellen
		// entsprechende methoden sollten die jeweiligen Datenbankcalls nutzen
		return Services.getInstance().getMessageService().getAll();
	}
}
