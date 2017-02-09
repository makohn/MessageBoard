package de.htwsaar.wirth.server.util.CommandFactory.CommandModel;

import java.rmi.RemoteException;

public interface Command {

	public void execute() throws RemoteException;
}
