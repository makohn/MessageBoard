package de.htwsaar.wirth.server.util;

import java.rmi.RemoteException;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;

public class PublishMessageCommand implements Command {
	
	private Message messageToPublish;
	private ParentServer parentToNotify;
	
	public PublishMessageCommand(ParentServer parentToNotify, Message messageToPublish) {
		this.messageToPublish = messageToPublish;
		this.parentToNotify = parentToNotify;
	}

	public void execute() throws RemoteException {
		if (parentToNotify == null || messageToPublish == null) {
			return;
		}
		parentToNotify.publish(messageToPublish);
	}
	
	public PublishMessageCommand clone() {
		return new PublishMessageCommand(parentToNotify, messageToPublish);
	}

	public void setNotifiable(Notifiable n) {
	}

}
