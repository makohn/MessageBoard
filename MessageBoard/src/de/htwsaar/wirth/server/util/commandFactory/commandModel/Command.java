package de.htwsaar.wirth.server.util.commandFactory.commandModel;

import java.rmi.RemoteException;

public interface Command {

	void execute() throws RemoteException;
	Command clone();
}
