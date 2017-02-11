package de.htwsaar.wirth.server.util.command.child;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.model.interfaces.Message;

import java.rmi.RemoteException;

public class EditMessageCommandChild extends ChildCommand {

    private Message message;
    private Notifiable childServer;

    public EditMessageCommandChild(Notifiable childServer, Message message) {
        this.message = message;
        this.childServer = childServer;
    }


    public void execute() throws RemoteException {
        childServer.notifyEdit(message);
    }

    @Override
    public void setNotifiable(Notifiable childServer) {
        this.childServer = childServer;
    }

    public ChildCommand clone() {
        return new EditMessageCommandChild(this.childServer,this.message);
    }
}


