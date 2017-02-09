package de.htwsaar.wirth.server.util.CommandFactory.CommandModel;

import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;

import java.rmi.RemoteException;

public class EditMessageCommandParent implements Command {

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


