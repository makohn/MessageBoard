package de.htwsaar.remote.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NotifiableClient extends Remote {
	public void notify(String message) throws RemoteException;
}
