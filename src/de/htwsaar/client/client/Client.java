package de.htwsaar.client.client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import de.htwsaar.remote.remote.NotifiableServer;

public class Client extends UnicastRemoteObject implements NotifiableServer {

	private static final long serialVersionUID = -6845076767542183660L;
	
	public String name;
	
	public Client(String name) throws RemoteException {
		super();
		this.name = name;
	}

	@Override
	public void notify(String message) throws RemoteException {
		print(message);
	}
	
	public void print(String msg) {
		System.out.println(msg);	
	}

}
