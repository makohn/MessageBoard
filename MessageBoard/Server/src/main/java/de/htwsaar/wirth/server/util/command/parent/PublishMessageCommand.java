package de.htwsaar.wirth.server.util.command.parent;

import java.rmi.RemoteException;

import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;

public class PublishMessageCommand extends ParentCommand {
	
	private Message messageToPublish;
	
	public PublishMessageCommand(ParentServer parentToNotify, Message messageToPublish) {
		this.messageToPublish = messageToPublish;
		this.parentToNotify = parentToNotify;
	}

	/**
	 * After a parent received this command it will be executed by the CommandRunner.
	 * @throws RemoteException
	 */
	public void execute() throws RemoteException {
		if (parentToNotify == null || messageToPublish == null) {
			return;
		}
		parentToNotify.publish(messageToPublish);
	}
	



}
