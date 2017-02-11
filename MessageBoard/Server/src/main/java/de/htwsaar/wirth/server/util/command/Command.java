package de.htwsaar.wirth.server.util.command;

import java.rmi.RemoteException;


public interface Command {
	/**
	 * The execute Command will be executed by the CommandRunner
	 * right after it was sent to an other server
	 * @throws RemoteException
	 */
	void execute() throws RemoteException;

}
