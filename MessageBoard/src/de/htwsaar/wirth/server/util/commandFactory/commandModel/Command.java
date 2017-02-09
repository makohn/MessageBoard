package de.htwsaar.wirth.server.util.commandFactory.commandModel;

import java.rmi.RemoteException;

public interface Command {

	public void execute() throws RemoteException;
}
