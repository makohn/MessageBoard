package de.htwsaar.wirth.client;

import java.rmi.NotBoundException;
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
import de.htwsaar.wirth.remote.util.RemoteConstants;
import javafx.application.Platform;

public class ClientImpl extends UnicastRemoteObject implements NotifiableClient {
	
    private static ClientImpl instance;
		
	private volatile MainViewController gui;
	private MessageBoard msgBoard;
	private AuthPacket auth;
	private String username;
	
	public static ClientImpl getInstance() {
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
	
	public String getUsername() {
		return username;
	}

	private static final long serialVersionUID = -7940206816319176143L;
	
	public void login(String username, String password, String parentHost, int parentPort) throws RemoteException, NotBoundException {
		
		if (msgBoard != null && auth != null) {
			msgBoard.logout(auth);
			msgBoard = null;
			auth = null;
			username = null;
		}
		
		// beim Server anmelden
		Registry parentRegistry = LocateRegistry.getRegistry(parentHost, parentPort);
        msgBoard = (MessageBoard) parentRegistry.lookup(RemoteConstants.BIND_KEY);
        
        this.username = username;
		
        // User einloggen
		LoginPacket login = new LoginPacket(username, password);
		auth = msgBoard.login(login, this);
	}
	
	public void logout() throws RemoteException {
		if (msgBoard != null) {
			msgBoard.logout(auth);
			msgBoard = null;
			auth = null;
		}
	}
	
	public List<Message> getAllMessages() throws RemoteException {
		return msgBoard.getMessages(auth);
	}
	
	public Map<String, Status> getUserStatus() throws RemoteException {
		return msgBoard.getUserStatus(auth);
	}
	
	public void addUser(String newUsername, String newPassword) throws RemoteException {
		if (msgBoard != null)
			msgBoard.addUser(auth, newUsername, newPassword);
	}
	
	public void deleteUser(String username) throws RemoteException {
		if (msgBoard != null)
			msgBoard.deleteUser(auth, username);
	}
	
	public void sendMessage(String msg) throws RemoteException {
		if (msgBoard != null)
			msgBoard.newMessage(auth, msg);
	}
	
	public void editMessage(String msg, UUID id) throws RemoteException {		
		if (msgBoard != null) {
			msgBoard.editMessage(auth, msg, id);
		}
	}

	public void publishMessage(UUID id) throws RemoteException {
		if (msgBoard != null)
			msgBoard.publish(auth, id);
	}
	
	
	public void deleteMessage(UUID id) throws RemoteException {
		if (msgBoard != null)
			msgBoard.deleteMessage(auth, id);
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
}
