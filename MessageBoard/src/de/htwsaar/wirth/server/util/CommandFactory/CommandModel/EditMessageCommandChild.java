package de.htwsaar.wirth.server.util.CommandFactory.CommandModel;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.model.interfaces.Message;

import java.rmi.RemoteException;

public class EditMessageCommandChild implements Command {

    private Message message;
    private Notifiable childserver;

    public EditMessageCommandChild(Notifiable childserver, Message message) {
        this.message = message;
        this.childserver = childserver;
    }

    public void execute() throws RemoteException {
        childserver.notifyEdit(message);
    }
}


