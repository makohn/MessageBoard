package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import api.Notifiable;

public class Client extends UnicastRemoteObject implements Notifiable {

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
