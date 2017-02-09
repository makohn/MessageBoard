package de.htwsaar.wirth.server.util.commandFactory.commandModel.parentCommand;

import java.rmi.RemoteException;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Command;

public class PublishMessageCommand extends ParentCommand {
	
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
	



}
