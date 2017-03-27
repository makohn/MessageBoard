package de.htwsaar.wirth.remote;

import java.rmi.RemoteException;

import de.htwsaar.wirth.remote.model.Status;

/**
 * Class {@code NotifiableClient} contains all methodes which can be called on a client to notify changes.
 */
public interface NotifiableClient extends Notifiable {
	void notifyUserStatus(String username, Status status) throws RemoteException;
	void notifyDeleteUser(String username) throws RemoteException;
}
