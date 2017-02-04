package de.htwsaar.wirth.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import de.htwsaar.wirth.remote.model.interfaces.Message;

public interface NotifiableClient extends Remote {
	
	public void notifyNew(Message msg) throws RemoteException;
	public void notifyDelete(Message msg) throws RemoteException;
	public void notifyEdit(Message msg) throws RemoteException;
}
