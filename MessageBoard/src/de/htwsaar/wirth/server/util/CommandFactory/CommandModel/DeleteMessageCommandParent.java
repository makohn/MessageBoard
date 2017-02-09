package de.htwsaar.wirth.server.util.CommandFactory.CommandModel;

import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;

import java.rmi.RemoteException;

public class DeleteMessageCommandParent implements Command {

    private Message messageToDelete;
    private ParentServer parent;

    public DeleteMessageCommandParent(ParentServer parent, Message messageToDelete) {
        this.messageToDelete = messageToDelete;
        this.parent = parent;
    }

    public void execute() throws RemoteException {
        parent.notifyServerDelete(messageToDelete);
    }
}


