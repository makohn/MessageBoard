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

public class ClientImpl /*extends UnicastRemoteObject*/ implements NotifiableClient {
	
    private static ClientImpl instance;
    
	private MainViewController gui;
	private MessageBoard msgBoard;
	private AuthPacket auth;
	
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
	
	public void setView(MainViewController gui) {
		this.gui = gui;
	}

	private static final long serialVersionUID = -7940206816319176143L;
	
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
	
	public Task<List<Message>> getAllMessages() {
		return new Task<List<Message>>() {
			@Override
			protected List<Message> call() throws Exception {
				return msgBoard.getMessages(auth);
			}
		};
	}
	
	public Task<Map<String, Status>> getUserStatus() {
		return new Task<Map<String, Status>>() {
			@Override
			protected Map<String, Status> call() throws Exception {
				return msgBoard.getUserStatus(auth);
			}
		};
	}
	
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
	
	// ----------------------- Notifiable ----------------------------------

	public void notifyNew(Message msg) throws RemoteException {
		Platform.runLater(() -> gui.insertMessage(msg));
	}

	public void notifyDelete(Message msg) throws RemoteException {
		Platform.runLater(() -> gui.deleteMessage(msg));		
	}

	public void notifyEdit(Message msg) throws RemoteException {
		Platform.runLater(() -> gui.editMessage(msg));
	}

	public void notifyUserStatus(String username, Status status) throws RemoteException {
		Platform.runLater(() -> gui.changeUserStatus(username, status));		
	}
	
	public void notifyDeleteUser(String username) throws RemoteException {
		Platform.runLater(() -> gui.deleteUser(username));
		
	}
	
	public String getUsername() {
		return auth.getUsername();
	}
	
	public boolean isGroupLeader() {
		return auth.isGroupLeader();
	}
	
	public String getGroupName() {
		return auth.getGroupName();
	}

	
}
