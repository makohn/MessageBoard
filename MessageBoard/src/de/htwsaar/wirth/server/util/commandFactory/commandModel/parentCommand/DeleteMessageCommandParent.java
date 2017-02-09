package de.htwsaar.wirth.server.util.commandFactory.commandModel.parentCommand;

import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Command;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.childCommand.DeleteMessageCommandChild;

import java.rmi.RemoteException;

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

    public ParentCommand clone() {
        return new DeleteMessageCommandParent(this.parent,this.messageToDelete);
    }
}


