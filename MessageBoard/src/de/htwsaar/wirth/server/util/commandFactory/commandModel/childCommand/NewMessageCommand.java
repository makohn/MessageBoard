package de.htwsaar.wirth.server.util.commandFactory.commandModel.childCommand;

import java.rmi.RemoteException;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Command;

public class NewMessageCommand extends ChildCommand {

	private Message message;
	private Notifiable childServer;

	public NewMessageCommand(Notifiable childServer, Message message) {
		this.message = message;
		this.childServer = childServer;
	}

	public void execute() throws RemoteException {
		childServer.notifyNew(message);
	}

	@Override
	public void setNotifiable(Notifiable childServer) {
		this.childServer = childServer;
	}

	public ChildCommand clone() {
		return new NewMessageCommand(this.childServer,this.message);
	}

}
