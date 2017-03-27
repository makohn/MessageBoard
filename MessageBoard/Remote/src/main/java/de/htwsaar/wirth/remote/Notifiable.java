package de.htwsaar.wirth.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import de.htwsaar.wirth.remote.model.interfaces.Message;

/**
 * Class {@code Notifiable} contains all methodes which can be called on a client or a server to notify changes.
 */
public interface Notifiable extends Remote {
	
	void notifyNew(Message msg) throws RemoteException;
	void notifyEdit(Message msg) throws RemoteException;
	void notifyDelete(Message msg) throws RemoteException;
}
