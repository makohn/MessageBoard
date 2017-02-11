package de.htwsaar.wirth.server.util.command.parent;

import java.rmi.RemoteException;

import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;

public class DeleteMessageCommandParent extends ParentCommand {

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


