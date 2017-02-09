package de.htwsaar.wirth.server.util.commandFactory.commandModel.parentCommand;

import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Command;

import java.rmi.RemoteException;

public class EditMessageCommandParent extends ParentCommand {

    private Message message;
    private ParentServer parent;

    public EditMessageCommandParent(ParentServer parent, Message message) {
        this.message = message;
        this.parent = parent;
    }

    public void execute() throws RemoteException {
        parent.notifyServerEdit(message);
    }


}


