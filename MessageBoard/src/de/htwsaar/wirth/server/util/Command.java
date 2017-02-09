package de.htwsaar.wirth.server.util;

import java.rmi.RemoteException;

import de.htwsaar.wirth.remote.Notifiable;

public interface Command extends Cloneable {
	public void execute() throws RemoteException;
	public void setNotifiable(Notifiable n);
	public Command clone();
}
