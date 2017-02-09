package de.htwsaar.wirth.server.util;

import java.rmi.RemoteException;

public interface Command {
	public void execute() throws RemoteException;
}
