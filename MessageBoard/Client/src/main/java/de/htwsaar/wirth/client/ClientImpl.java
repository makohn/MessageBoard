package de.htwsaar.wirth.client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import de.htwsaar.wirth.client.controller.MessageObserver;
import de.htwsaar.wirth.client.controller.MessageProvider;
import de.htwsaar.wirth.remote.MessageBoard;
import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.model.auth.AuthPacket;
import de.htwsaar.wirth.remote.model.auth.LoginPacket;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.remote.util.RemoteConstants;

public class ClientImpl extends UnicastRemoteObject implements Notifiable, MessageProvider  {
	
	private static ClientImpl instance;
	private MessageBoard msgBoard;
	private AuthPacket auth;
	private List<Message> messages;
	private MessageObserver gui;
	
	private ClientImpl() throws RemoteException {
		messages = new ArrayList<Message> ();
	}
	
	public static synchronized ClientImpl getInstance() {
		if(instance == null) {
			try {
				instance = new ClientImpl();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return instance;
	}

	private static final long serialVersionUID = -7940206816319176143L;
	
	public void connect(LoginPacket login/*, String host, int port*/) {
		try {
			Registry rmiRegistry = LocateRegistry.getRegistry("localhost", 1099);
			msgBoard = (MessageBoard) rmiRegistry.lookup(RemoteConstants.BIND_KEY);
			auth = msgBoard.registerClient(login, ClientImpl.getInstance());
			messages.addAll(msgBoard.getMessages(auth));
		}
		catch (Exception e) {
			
		}
	}

	public void notifyNew(Message msg) throws RemoteException {
		gui.notifyNew(msg);
	}

	public void notifyDelete(Message msg) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	public void notifyEdit(Message msg) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void sendMessage(String msg) {
		try {
			msgBoard.newMessage(auth, msg);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<Message> register(MessageObserver observer) {
		this.gui = observer;
		return messages;
	}
}
