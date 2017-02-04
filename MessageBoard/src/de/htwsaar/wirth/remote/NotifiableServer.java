package de.htwsaar.wirth.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import de.htwsaar.wirth.remote.model.interfaces.Message;

public interface NotifiableServer extends Remote {
	
	public void notifyServerDelete (Message msg) throws RemoteException;
	public void notifyServerEdit (Message msg) throws RemoteException;
	public void publish(Message msg);
	
}
