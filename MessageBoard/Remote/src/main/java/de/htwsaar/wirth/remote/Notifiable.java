package de.htwsaar.wirth.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import de.htwsaar.wirth.remote.model.Status;
import de.htwsaar.wirth.remote.model.interfaces.Message;

public interface Notifiable extends Remote {
	
	void notifyNew(Message msg) throws RemoteException;
	void notifyEdit(Message msg) throws RemoteException;
	void notifyDelete(Message msg) throws RemoteException;
	void notifyUserStatus(String username, Status status) throws RemoteException;
}
