package de.htwsaar.wirth.client;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.htwsaar.wirth.client.controller.MainViewController;
import de.htwsaar.wirth.remote.MessageBoard;
import de.htwsaar.wirth.remote.NotifiableClient;
import de.htwsaar.wirth.remote.model.Status;
import de.htwsaar.wirth.remote.model.auth.AuthPacket;
import de.htwsaar.wirth.remote.model.auth.LoginPacket;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import javafx.application.Platform;
import javafx.concurrent.Task;

/**
 * {@code ClientImpl} represents a client instance. It is responsible for 
 * sending requests to the server and receiving callback notifications. As such it
 * servers as a broker between the UI classes and the {@code MessageBoard} instance.
 * 
 */
public class ClientImpl implements NotifiableClient {
	
    private static ClientImpl instance;
    
	private MainViewController gui;
	private MessageBoard msgBoard;
	private AuthPacket auth;
	
	/**
	 * Singleton access method
	 * @return instance - the single unique instance of this class
	 */
	public static synchronized ClientImpl getInstance() {
		if (instance == null) {
			try {
				instance = new ClientImpl();
			} catch (RemoteException e) {}
		}
		return instance;
	}

	protected ClientImpl() throws RemoteException {
	}
	
	/**
	 * {@code setView} attaches the UI to this client.
	 * @param gui - the UI facade class to be accessed via the client
	 */
	public void setView(MainViewController gui) {
		this.gui = gui;
	}
	
	/**
	 * {@code login} exports this instance as a {@code UnicastRemoteObject} and binds
	 * it to an either specified or arbitrary port. It then sends a {@code login} request 
	 * to the {@code MessageBoard} in order to couple both of them. 
	 * @param username - the username that is user to identify the user of this client
	 * @param password - the password of this user
	 * @param parentHost - the host address of the server this client tries to connect to
	 * @param port - the port the exported {@code UnicastRemoteObject} is listening on
	 * @param groupName - the group name of the {@code MessageBoard} instance this client
	 * 					  tries to connect to.
	 * @return a login task that is executed on a non-UI thread but can be called within a UI thread
	 */
	public Task<Void> login(String username, String password, String parentHost, int port,String groupName) {
		
		ClientImpl thisReference = this;
		
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				if (msgBoard != null && auth != null) {
					Task<Void> logoutTask = logout();
					logoutTask.run();
				}
				
				try {
					UnicastRemoteObject.unexportObject(thisReference, true);
				} catch (NoSuchObjectException e) {}
				
				UnicastRemoteObject.exportObject(thisReference, port);
				// beim Server anmelden
				Registry parentRegistry = LocateRegistry.getRegistry(parentHost);
		        msgBoard = (MessageBoard) parentRegistry.lookup(groupName);

		        
		        // User einloggen
				LoginPacket login = new LoginPacket(username, password);
				auth = msgBoard.login(login, thisReference);
				return null;
			}
		};
	}
	
	/**
	 * {@code logout} sends a {@code logout} request to the {@code MessageBoard} 
	 * in order to close the connection.
	 * @return a logout task that is executed on a non-UI thread but can be called 
	 * 		   within a UI thread
	 */
	public Task<Void> logout() {
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				if (msgBoard != null) {
					msgBoard.logout(auth);
					msgBoard = null;
					auth = null;
				}
				return null;
			}
		};
	}
	
	/**
	 * {@code getAllMessages} sends a {@code getAllMessages} request to the {@code MessageBoard} 
	 * @return a {@code getMessages} task that is executed on a non-UI thread but can be called 
	 * 		   within a UI thread
	 */
	public Task<List<Message>> getAllMessages() {
		return new Task<List<Message>>() {
			@Override
			protected List<Message> call() throws Exception {
				return msgBoard.getMessages(auth);
			}
		};
	}
	
	/**
	 * {@code getUserStatus} sends a {@code getUserStatus} request to the {@code MessageBoard} 
	 * @return a {@code getUserStatus} task that is executed on a non-UI thread but can be called 
	 * 		   within a UI thread
	 */
	public Task<Map<String, Status>> getUserStatus() {
		return new Task<Map<String, Status>>() {
			@Override
			protected Map<String, Status> call() throws Exception {
				return msgBoard.getUserStatus(auth);
			}
		};
	}
	
	/**
	 * {@code addUser} sends a {@code addUser} request to the {@code MessageBoard} 
	 * @return a {@code addUser} task that is executed on a non-UI thread but can be called 
	 * 		   within a UI thread
	 */
	public Task<Void> addUser(String newUsername, String newPassword) {
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				if (msgBoard != null)
					msgBoard.addUser(auth, newUsername, newPassword);
				return null;
			}
			
		};
	}
	
	/**
	 * {@code deleteUser} sends a {@code deleteUser} request to the {@code MessageBoard} 
	 * @return a {@code deleteUser} task that is executed on a non-UI thread but can be called 
	 * 		   within a UI thread
	 */
	public Task<Void> deleteUser(String username) {
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				if (msgBoard != null)
					msgBoard.deleteUser(auth, username);
				return null;
			}
			
		};
	}
	
	/**
	 * {@code sendMessage} sends a {@code newMessage} request to the {@code MessageBoard} 
	 * @return a {@code sendMessage} task that is executed on a non-UI thread but can be called 
	 * 		   within a UI thread
	 */
	public Task<Void> sendMessage(String msg) {
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				if (msgBoard != null)
					msgBoard.newMessage(auth, msg);
				return null;
			}
			
		};
	}
	
	/**
	 * {@code editMessage} sends a {@code editMessage} request to the {@code MessageBoard} 
	 * @return a {@code editMessage} task that is executed on a non-UI thread but can be called 
	 * 		   within a UI thread
	 */
	public Task<Void> editMessage(String msg, UUID id) {
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				if (msgBoard != null)
					msgBoard.editMessage(auth, msg, id);
				return null;
			}
			
		};
	}
	
	/**
	 * {@code publishMessage} sends a {@code publish} request to the {@code MessageBoard} 
	 * @return a {@code publishMessage} task that is executed on a non-UI thread but can be called 
	 * 		   within a UI thread
	 */
	public Task<Void> publishMessage(UUID id) {
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				if (msgBoard != null)
					msgBoard.publish(auth, id);
				return null;
			}
		};
	}
	
	/**
	 * {@code deleteMessage} sends a {@code deleteMessage} request to the {@code MessageBoard} 
	 * @return a {@code deleteMessage} task that is executed on a non-UI thread but can be called 
	 * 		   within a UI thread
	 */
	public Task<Void> deleteMessage(UUID id) {
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				if (msgBoard != null)
					msgBoard.deleteMessage(auth, id);
				return null;
			}
			
		};
	}
	
	/**
	 * {@code changeUserStatus} sends a {@code changeUserStatus} request to the {@code MessageBoard} 
	 * @return a {@code changeUserStatus} task that is executed on a non-UI thread but can be called 
	 * 		   within a UI thread
	 */
	public Task<Void> changeUserStatus(Status status) {
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				if (msgBoard != null)
					msgBoard.changeUserStatus(auth, status);
				return null;
			}
		};
	}
	
	//================================================================================
    // Notifiable implemented methods
    //================================================================================

	/**
	 * {@code notifyNew} is a callback method. Triggers the UI to add a new {@code Message}. 
	 */
	public void notifyNew(Message msg) throws RemoteException {
		Platform.runLater(() -> gui.insertMessage(msg));
	}

	/**
	 * {@code notifyDelete} is a callback method. Triggers the UI to delete a {@code Message}. 
	 */
	public void notifyDelete(Message msg) throws RemoteException {
		Platform.runLater(() -> gui.deleteMessage(msg));		
	}

	/**
	 * {@code notifyEdit} is a callback method. Triggers the UI to adapt an edited {@code Message}. 
	 */
	public void notifyEdit(Message msg) throws RemoteException {
		Platform.runLater(() -> gui.editMessage(msg));
	}

	/**
	 * {@code notifyUserStatus} is a callback method. 
	 * Triggers the UI to adapt the user {@code Status} of a specified {@code User}. 
	 */
	public void notifyUserStatus(String username, Status status) throws RemoteException {
		Platform.runLater(() -> gui.changeUserStatus(username, status));		
	}
	
	/**
	 * {@code notifyNew} is a callback method. Triggers the UI to delete a specified {@code User}. 
	 */
	public void notifyDeleteUser(String username) throws RemoteException {
		Platform.runLater(() -> gui.deleteUser(username));
	}
	
	//================================================================================
    // Getter methods
    //================================================================================
	
	public String getUsername() {
		return auth.getUsername();
	}
	
	public boolean isGroupLeader() {
		return auth.isGroupLeader();
	}
	
	public String getGroupName() {
		return auth.getGroupName();
	}
	
	public boolean isConnectedToRoot() {
		return auth.isConnectedToRoot();
	}
}
