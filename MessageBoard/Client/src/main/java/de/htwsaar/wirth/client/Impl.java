package de.htwsaar.wirth.client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import de.htwsaar.wirth.client.controller.MainViewController;
import de.htwsaar.wirth.remote.MessageBoard;
import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.model.auth.AuthPacket;
import de.htwsaar.wirth.remote.model.auth.LoginPacket;
import de.htwsaar.wirth.remote.model.interfaces.Message;

public class Impl extends UnicastRemoteObject implements Notifiable {
	
	private AuthPacket auth;
	private MainViewController gui;
	private MessageBoard parent;

	protected Impl() throws RemoteException {
		// TODO Auto-generated constructor stub
		// login
	}

	private static final long serialVersionUID = -7940206816319176143L;
	
	public void login(String username, String password) throws RemoteException {
		LoginPacket login = new LoginPacket(username, password);
		auth = parent.registerClient(login, this);		
	}
	
	public void sendMessage() {
		
	}
	
	public void editMessage() {
		
	}
	
	public void deleteMessage() {
		
	}
	
	// ----------------------- Notifiable ----------------------------------

	public void notifyNew(Message msg) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	public void notifyDelete(Message msg) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	public void notifyEdit(Message msg) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
}
