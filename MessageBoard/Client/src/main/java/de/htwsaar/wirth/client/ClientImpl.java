package de.htwsaar.wirth.client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import de.htwsaar.wirth.remote.MessageBoard;
import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.model.auth.LoginPacket;
import de.htwsaar.wirth.remote.model.interfaces.Message;

public class ClientImpl extends UnicastRemoteObject implements Notifiable {
	
	private static ClientImpl instance;
	
	private ClientImpl() throws RemoteException {
		// TODO Auto-generated constructor stub
	}
	
	public static synchronized ClientImpl getInstance() throws RemoteException {
		if(instance == null) {
			instance = new ClientImpl();
		}
		return instance;
	}

	private static final long serialVersionUID = -7940206816319176143L;
	
	public void connect(LoginPacket login) {
		try {
			Registry rmiRegistry = LocateRegistry.getRegistry("localhost", 1099);
			MessageBoard msgBoard = (MessageBoard) rmiRegistry.lookup("MessageBoard");
			msgBoard.registerClient(login, ClientImpl.getInstance());
		}
		catch (Exception e) {
			
		}
	}

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
