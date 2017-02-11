package de.htwsaar.wirth.server.util.command.parent;

import java.rmi.RemoteException;

import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;

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


