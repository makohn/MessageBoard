package de.htwsaar.wirth.server;

import java.rmi.RemoteException;

import de.htwsaar.wirth.remote.Notifiable;

public interface ClientNotifyHandler {
	
	public void handle(Notifiable client) throws RemoteException;

}
