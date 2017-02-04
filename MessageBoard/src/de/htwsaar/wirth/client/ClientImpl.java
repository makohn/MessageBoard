package de.htwsaar.wirth.client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import de.htwsaar.wirth.remote.NotifiableClient;
import de.htwsaar.wirth.remote.model.interfaces.Message;

public class ClientImpl extends UnicastRemoteObject implements NotifiableClient {

	protected ClientImpl() throws RemoteException {
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = -7940206816319176143L;

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
