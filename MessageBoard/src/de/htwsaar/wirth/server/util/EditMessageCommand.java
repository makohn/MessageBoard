package de.htwsaar.wirth.server.util;

import java.rmi.RemoteException;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;

public class EditMessageCommand implements Command {

	private Message messageToEdit;
	private Notifiable childServer;
	private ParentServer parentToNotify;

	public EditMessageCommand(Notifiable childServer, Message messageToEdit) {
		this.messageToEdit = messageToEdit;
		this.childServer = childServer;
	}

	public EditMessageCommand(ParentServer parentToNotify, Message messageToEdit) {
		this.messageToEdit = messageToEdit;
		this.parentToNotify = parentToNotify;
	}

	public void execute() throws RemoteException {
		if (messageToEdit != null) {
			if (parentToNotify != null) {
				parentToNotify.notifyServerEdit(messageToEdit);
			} else if (childServer != null) {
				childServer.notifyEdit(messageToEdit);
			}
		}
	}

}
