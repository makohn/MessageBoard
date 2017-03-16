package de.htwsaar.wirth.remote;

import java.rmi.RemoteException;

import de.htwsaar.wirth.remote.model.Status;

public interface NotifiableClient extends Notifiable {
	void notifyUserStatus(String username, Status status) throws RemoteException;
	void notifyDeleteUser(String username) throws RemoteException;
}
