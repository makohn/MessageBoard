package de.htwsaar.wirth.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

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

/**
 * {@code MessageBoardImpl} implements the {@code MessageBoard} interface and as such 
 * provides all the functionality that could be called remotely in order to r/w the 
 * data model.{@code MessageBoardImpl} also manages several lists of client instances
 * and child servers in order to implement a callback mechanism.
 * 
 * @author janibal, Schloesser, philippschaefer4
 */
public class MessageBoardImpl implements Notifiable, MessageBoard, ParentServer {
	
	private static final Logger logger = LogManager.getLogger(MessageBoardImpl.class);
	
	/**
	 * the {@code SessionManager} for the {@MessageBoard}
	 */
	private SessionManager sessionManager;

	/**
	 * childServerList with each {@code ChildServer}
	 */
	private List<Notifiable> childServerList;

	/**
	 * a {@code Map} with CommandRunners used to lookup a {@code CommandRunner} by its
	 * {@code Notifiable} each {@code CommandRunner} is a thread, which executes every 
	 * {@code Command} it gets.
	 */
	private Map<Notifiable, CommandRunner> childServerQueueMap;

	/**
	 * the parent of this server {@code null}, if this server is the root-server
	 */
	private ParentServer parent;

	/**
	 * the {@code Queue} to send commands to the parent
	 */
	private CommandRunner parentQueue;

	/**
	 * a list of clients
	 */
	private Map<String, NotifiableClient> clientNotifyMap;
	
	/**
	 * a map to manage the {@code Status} of each user
	 */
	private Map<String, Status> userStatus;

	/**
	 * a {@code ThreadPool} we use for callbacks to clients
	 */
	private ExecutorService threadPool;

	/**
	 * Constructor of {@code MessageBoardImpl}. Exports this instance as
	 * a {@code UnicastRemoteObject} and binds it to the specified port in order
	 * to make it accessible to incoming calls. 
	 * @param groupName - the specified groupName of this server instance
	 * @param localPort - the specified port this instance is listening on
	 * @throws RemoteException
	 */
	public MessageBoardImpl(String groupName, int localPort) throws RemoteException {
		logger.info("Initialising the MessageBoard instance...");
		sessionManager = new SessionManager(groupName);
		
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
		logger.info("Successfully exported the MessageBoardImpl as UnicastRemoteObject.");
		logger.info("MessageBoardImpl is listening on port " + localPort);
	}
	
	
	//================================================================================
    // Auxiliary methods
    //================================================================================
	/**
	 * {@code addParent} is called by this server instance in order to add the specified
	 * instance as its parent.
	 * @param parent - a {@code ParentServer} instance that is requested to be this 
	 * 				   instance's parent.
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

	/**
	 * {@code syncParent} compares the messages of the {@code ParentServer} with the own ones and adapts
	 * all changes if there are any. This method is usually called during the connection process
	 * to a parent server, i.e. during a initial start or a restart of the server instance.
	 * @throws RemoteException
	 */
	private void syncParent() throws RemoteException {
		int count = 0;
		List<Message> parentMessages = parent.getMessages();
		List<Message> childMessages = Services.getInstance().getMessageService().getAll();
		
		// create a map from the childMsgs
		Map<UUID, Message> map = new HashMap<>();
		for(Message childMsg : childMessages) {
			map.put(childMsg.getID(), childMsg);
		}
		
		// iterate over the parentMessages and check, if the parentMsg is newer than the childMsg
		for(Message parentMsg : parentMessages) {
			count++;
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
		logger.info(count + " messages were checked");
	}

	/**
	 * {@code isRoot} checks, whether the current Server is the root-server
	 */
	private boolean isRoot() {
		return this.parent == null;
	}
	
	/**
	 * {@code notifyClients} notifies all clients of this server instance. Each notification
	 * process should be handled by its own worker thread. The action that should be performed
	 * within this notification is represented by the {@code ClientNotifyHandler}. As this being
	 * a single-method interface, the action might be defined as a lambda expression.
	 * @param handler - a {@code ClientNotifyHandler} implementation specifying the action to be
	 * 					performed. Could be implemented as lambda.
	 */
	private void notifyClients(ClientNotifyHandler handler) {
		for (Entry<String, NotifiableClient> entry : clientNotifyMap.entrySet()) {
				threadPool.execute(() -> {
					try {
						handler.handle(entry.getValue());
					} catch (RemoteException e) {
						String s = entry.getValue().toString();
						logger.warn("Client: " + entry.getKey() + " @endpoint: " 
								    + s.substring(s.indexOf("endpoint") + 9, s.indexOf(']') + 1) 
								    + " could not be resolved.");
						// if we catch a remoteException the callback for this client doesn't work
						if (clientNotifyMap.containsValue(entry.getValue())) {
							clientNotifyMap.remove(entry);
							//changeUserStatusAndNotifyClients(entry.getKey(), Status.SHOW_AS_OFFLINE);
						}
					} catch (NoSuchElementException ex) {
						logger.warn(entry.getKey() + " tried to execute a method on a non-existing object");
						logger.debug(ex.getStackTrace());
					}
				});
			}
	}

	/**
	 * {@code queueCommandForAllChildServer} adds a specified command to a queue, that tries
	 * to execute the command as soon as possible. This process is done for each {@code Notifiable}
	 * child of this servers. (Each one of them has a corresponding queue)
	 * @param cmd - a {@code ChildCommand} that wraps a certain action to be executed on a
	 * 				child server
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
	 * {@code changeUserStatusAndNotifyClients} changes the status of a specified {@code User} 
	 * and also notifies the clients of this server about the change.
	 * @param username - the unique identifier of the {@code User}
	 * @param status - one of the following {@code Status} values: ONLINE, OFFLINE, AWAY, BUSY
	 */
	private void changeUserStatusAndNotifyClients(String username, Status status) {
		userStatus.put(username, status);
		notifyClients(cl -> cl.notifyUserStatus(username, status));
	}

	/**
	 * {@code needToSendParent} decides whether a {@code Message} has to be sent to the parent.
	 * @param msg - the {@code Message} that should be checked
	 */
	private boolean needToSendParent(Message msg){
		// FIXME: add condition Database has msg
		// evtl. grundsätzlich hochzuschieben und ggf. ignorieren
		return (msg.isPublished() && !isRoot());
	}

	/**
	 * {@code needToPublish} decides whether a {@code Message} has to be published to the parent.
	 * @param msg - the {@code Message} that should be checked
	 */
	private boolean needToPublish(Message msg){
		// add condition Database has msg
		return (!msg.isPublished() && !isRoot());
	}
	
	//================================================================================
    // MessageBoard implemented methods
    //================================================================================
	
	/**
	 * {@code logout} is an implemented method of {@code MessageBoard}.
	 * This method is remote-called by a client to login to its group (server)
	 * and establish the corresponding connection. The server will then add the client
	 * to its list of connected clients.
	 * @param login - a {@code LoginPacket} providing the necessary user details
	 * @param client - the {@code NotifableClient} instance that sends the request.
	 * @throws RemoteException
	 */
	public AuthPacket login(LoginPacket login, NotifiableClient client) throws RemoteException {
		logger.debug("user " + login.getUsername() + " requests login.");
		// Authenticate throws an exception, if the username or password are wrong
		// this exception can be handled on clientside
		AuthPacket auth = sessionManager.authenticate(login);
		changeUserStatusAndNotifyClients(login.getUsername(), Status.ONLINE);
		// add the Notifiable of the client to the clientNotifyMap
		clientNotifyMap.put(login.getUsername(), client);
		return auth;
	}
	
	/**
	 * {@code logout} is an implemented method of {@code MessageBoard}.
	 * This method is remote-called by a client to logout and closes the corresponding connection. 
	 * @param auth - an {@code AuthPacket} given by the login. Identifies the
	 * 				 caller of this method.
	 * @param status - one of the following {@code Status} values: ONLINE, OFFLINE, AWAY, BUSY
	 * @throws RemoteException
	 */
	public void logout(AuthPacket auth) throws RemoteException {
		logger.debug("user " + auth.getUsername() + " requests logout.");
		sessionManager.verifyAuthPacket(auth);
		// remove the user from the clientNotifyMap and set his status to offline
		clientNotifyMap.remove(auth.getUsername());
		sessionManager.logout(auth.getUsername());
		
		changeUserStatusAndNotifyClients(auth.getUsername(), Status.OFFLINE);
	}
	
	/**
	 * {@code changeUserStatus} is an implemented method of {@code MessageBoard}.
	 * This method is remote-called by a client to change the status of a {@code User}. 
	 * Note that only the user itself is allowed to perform this action.
	 * @param auth - an {@code AuthPacket} given by the login. Identifies the
	 * 				 caller of this method.
	 * @param status - one of this {@code Status} values: ONLINE, OFFLINE, AWAY, BUSY
	 * @throws RemoteException
	 */
	public void changeUserStatus(AuthPacket auth, Status status) throws RemoteException {
		logger.debug("user::" + auth.getUsername() + " Status: " + status);
		sessionManager.verifyAuthPacket(auth);
		String username = auth.getUsername();
		changeUserStatusAndNotifyClients(username, status);
	}
		
	/**
	 * {@code addUser} is an implemented method of {@code MessageBoard}.
	 * This method is remote-called by a client to add a {@code User}. A new 
	 * user is created by providing its {@code password} and {@code username}. 
	 * Note that only the administrator of the belonging group is allowed to
	 * perform this action. The default {@code Status} is set to {@code Status.OFFLINE}
	 * @param auth - an {@code AuthPacket} given by the login. Identifies the
	 * 				 caller of this method.
	 * @param newUsername - the unique identifier for the new created {@code User}.
	 * @param newPassword - the password of the new created {@code User}
	 * @throws RemoteException
	 */
	public void addUser(AuthPacket auth, String newUsername, String newPassword) throws RemoteException{
		logger.debug("Add new user: " + newUsername);
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
	
	/**
	 * {@code deleteUser} is an implemented method of {@code MessageBoard}.
	 * This method is remote-called by a client to delete a {@code User}, specified by
	 * its unique {@code username}. Note that only the administrator of the belonging 
	 * group is allowed to perform this action. 
	 * @param auth - an {@code AuthPacket} given by the login. Identifies the
	 * 				 caller of this method.
	 * @param username - the unique identifier for a {@code User}.
	 * @throws RemoteException
	 */
	public void deleteUser(AuthPacket auth, String username) throws RemoteException {
		logger.debug("Delete user: " + username);
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
	 * {@code publish} is an implemented method of {@code MessageBoard}.
	 * This method is remote-called by a client to publish a {@code Message}, specified by
	 * its {@code UUID}.Note that only the administrator of the belonging group 
	 * is allowed to perform this action.
	 * @param auth - an {@code AuthPacket} given by the login. Identifies the
	 * 				 caller of this method.
	 * @param id - the {@code UUID} of this message
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
	 * {@code newMessage} is an implemented method of {@code MessageBoard}.
	 * This method is remote-called by a client to create a new {@code Message},
	 * @param auth - an {@code AuthPacket} given by the login. Identifies the
	 * 				 caller of this method.
	 * @param msg - the textual content of the new {@code Message}
	 * @throws RemoteException
	 */
	public void newMessage(AuthPacket auth, String msg) throws RemoteException {
		sessionManager.verifyAuthPacket(auth);
		Message message = new MessageImpl(msg, auth.getUsername(), sessionManager.getGroupName());
		notifyNew(message);
	}
	
	/**
	 * {@code editMessage} is an implemented method of {@code MessageBoard}. 
	 * This method is remote-called by a client to edit a specified {@code Message},
	 * identified by its {@code UUID}. Note that only the author of the message or
	 * is allowed to perform this action.
	 * @param auth - an {@code AuthPacket} given by the login. Identifies the
	 * 				 caller of this method.
	 * @param msgText - the new text of the edited {@code Message}
	 * @param id - the {@code UUID} of this message
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
	 * {@code deleteMessage} is an implemented method of {@code MessageBoard}. 
	 * This method is remote-called by a client to delete a specified {@code Message},
	 * identified by its {@code UUID}. Note that only the author of the message or
	 * the administrator of the belonging group are allowed to perform this action.
	 * @param auth - an {@code AuthPacket} given by the login. Identifies the
	 * 				 caller of this method.
	 * @param id - the {@code UUID} of this message
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
	 * {@code getMessages} is an implemented method of {@code MessageBoard}. 
	 * This method is remote-called by a client to receive a list of {@code Message}
	 * objects that are stored in the database.
	 * @param auth - an {@code AuthPacket} given by the login. Identifies the
	 * 				 caller of this method.
	 * @return - a {@code List} of {@code Message} Objects
	 * @throws RemoteException
	 */
	@Override
	public List<Message> getMessages(AuthPacket auth) throws RemoteException {
		sessionManager.verifyAuthPacket(auth);
		return getMessages();
	}
		
	public Map<String, Status> getUserStatus(AuthPacket auth) throws RemoteException {
		sessionManager.verifyAuthPacket(auth);
		return userStatus;
	}
	
	//================================================================================
    // Notifiable implemented methods
    //================================================================================

	/**
	 * {@code notifyNew} is an implemented method of {@code Notifiable}.  
	 * This method is remote-called by a client or this instance itself in order to
	 * handle a new message. Besides of storing the new message in this server's database,
	 * {@code Notifiable} clients of this server are also notified about the new message.
	 * @param msg - the {@code Message} to be created.
	 * @throws RemoteException
	 */
	@Override
	public void notifyNew(Message msg) throws RemoteException {
		Services.getInstance().getMessageService().saveMessage(msg);

		// Add a NewMessageCommand to each CommandRunner
		queueCommandForAllChildServer(CommandBuilder.buildChildCommand(msg, ChildCmd.NEW));

		// Notify each client
		notifyClients(cl -> cl.notifyNew(msg));
	}

	/**
	 * {@code notifyEdit} is an implemented method of {@code Notifiable}. 
	 * This method is remote-called by a client or this instance itself in order to
	 * handle an edited message. Besides of storing the edited message in this server's database,
	 * {@code Notifiable} clients of this server are also notified about the edited message.
	 * @param msg - the {@code Message} that is edited.
	 * @throws RemoteException
	 */	
	@Override
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
	 * {@code notifyDelete} is an implemented method of {@code Notifiable}. 
	 * This method is remote-called by a client or this instance itself in order to 
	 * handle a requested deletion of a message. Besides of deleting the message from
	 * this server's database, {@code Notifiable} clients of this server are also notified 
	 * about the deleted message.
	 * @param msg - the {@code Message} that should be deleted.
	 * @throws RemoteException
	 */
	@Override
	public void notifyDelete(Message msg) throws RemoteException {
		Services.getInstance().getMessageService().deleteMessage(msg);
		// Add a DeleteMessageCommand to each CommandRunner
		queueCommandForAllChildServer(CommandBuilder.buildChildCommand(msg, ChildCmd.DELETE));
		// Notify each client
		notifyClients(cl -> cl.notifyDelete(msg));
	}
	
	//================================================================================
    // ParentServer implemented methods
    //================================================================================
	
	/**
	 * {@code registerServer} is an implemented method of {@code ParentServer}. 
	 * This method is remote-called by a child-server to bind itself to its parent (this).
	 * The registration is important for the notify process
	 *
	 * @param childServer - a {@code Notifiable} server requesting the binding
	 * @throws RemoteException 
	 */
	@Override
	public void registerServer(Notifiable childServer) throws RemoteException {
		childServerList.add(childServer);
		CommandRunner commandRunner = new CommandRunner();
		commandRunner.start();
		childServerQueueMap.put(childServer, commandRunner);
	}

	/**
	 * {@code publish} is an implemented method of {@code ParentServer}.
	 * This method is remote-called by a child-server in order to publish a message.
	 * The message then is stored in the server's database. Also, {@code Notifiable}
	 * clients of this server are notified about the new message.
	 * If the message is already published this method has no effect.
	 * @param msg
	 * @throws RemoteException
	 */
	@Override
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
	 * {@code notifyServerEdit} is an implemented method of {@code ParentServer}.
	 * This method is remote-called by a child-server each time the child-server receives
	 * an edited message. The server will then handle the edited message via its
	 * {@code notifyEdit} method. 
	 * It then checks whether it is necessary to send the edited message to its parent.
	 * If so, the corresponding command is built via the {@code CommandBuilder} and sent
	 * to this server's parent.
	 * @param msg - the edited {@code Message} Object 
	 * @throws RemoteException
	 */
	@Override
	public void notifyServerEdit(Message msg) throws RemoteException {
		notifyEdit(msg);
		if(needToSendParent(msg)) {
			Command cmd = CommandBuilder.buildParentCommand(parent, msg, ParentCmd.EDIT);
			parentQueue.addCommand(cmd);
		}
	}

	/**
	 * {@code notifyServerDelete} is an implemented method of {@code ParentServer}.
	 * This method is remote-called by a child-server each time the child-server receives
	 * a deletion call. If the message exists in this server's database, it will be handled via the
	 * {@code notifyDelete} method. 
	 * If this server is not the root server, the corresponding delete command is built 
	 * via the {@code CommandBuilder} and sent to this server's parent.
	 * @param msg - the deleted {@code Message} Object 
	 * @throws RemoteException
	 */
	@Override
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

	/**
	 * {@code getMessages} is an implemented method of {@code ParentServer}.
	 * This method is remote-called by a child-server in order to receive all the messages
	 * that are stored in this server's database.
	 * @throws RemoteException
	 */
	public List<Message> getMessages() throws RemoteException {
		// TODO: wir sollten jeweils methoden mit limitierung und filterung nach gruppe erstellen
		// entsprechende methoden sollten die jeweiligen Datenbankcalls nutzen
		return Services.getInstance().getMessageService().getAll();
	}
}
