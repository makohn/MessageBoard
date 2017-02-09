package de.htwsaar.wirth.server.util.CommandFactory.CommandModel;

import java.rmi.RemoteException;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.CommandFactory.CommandModel.Command;

public class NewMessageCommand implements Command {

	private Message message;
	private Notifiable childserver;

	public NewMessageCommand(Notifiable childserver, Message message) {
		this.message = message;
		this.childserver = childserver;
	}

	public void execute() throws RemoteException {
		childserver.notifyNew(message);
	}

}
