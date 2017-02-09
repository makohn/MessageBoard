package de.htwsaar.wirth.server.util;

import java.rmi.RemoteException;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;

public class DeleteMessageCommand implements Command {
	
	private Message messageToDelete;
	private Notifiable childServer;
	private ParentServer parentToNotify;
	
	public DeleteMessageCommand(Message messageToDelete) {
		this.messageToDelete = messageToDelete;
	}

	public DeleteMessageCommand(Notifiable childServer, Message messageToDelete) {
		this.messageToDelete = messageToDelete;
		this.childServer = childServer;
	}

	public DeleteMessageCommand(ParentServer parentToNotify, Message messageToEdit) {
		this.messageToDelete = messageToEdit;
		this.parentToNotify = parentToNotify;
	}
	
	public void setNotifiable(Notifiable childServer) {
		this.childServer = childServer;
	}

	public void execute() throws RemoteException {
		if (messageToDelete != null) {
			if (parentToNotify != null) {
				parentToNotify.notifyServerDelete(messageToDelete);
			} else if (childServer != null) {
				childServer.notifyDelete(messageToDelete);
			}
		}
	}
	
	public DeleteMessageCommand clone() {
		return new DeleteMessageCommand(this.messageToDelete);
	}
	
}
