package de.htwsaar.wirth.server.util;

import java.rmi.RemoteException;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.model.interfaces.Message;

public class NewMessageCommand implements Command {
	
	private Message message;
	private Notifiable serverToCall;
	
	public NewMessageCommand(Notifiable serverToCall, Message message) {
		this.serverToCall = serverToCall;
		this.message = message;
	}

	public void execute() throws RemoteException {
		if (serverToCall == null || message == null) {
			return;
		}
		serverToCall.notifyNew(message);
	}

}
