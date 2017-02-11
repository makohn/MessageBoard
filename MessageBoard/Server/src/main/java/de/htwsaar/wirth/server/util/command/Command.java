package de.htwsaar.wirth.server.util.command;

import java.rmi.RemoteException;

public interface Command {

	void execute() throws RemoteException;

}
