package de.htwsaar.wirth.server.util.command.child;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.model.interfaces.Message;

import java.rmi.RemoteException;

public class DeleteMessageCommandChild extends ChildCommand {

    private Message messageToDelete;
    private Notifiable childServer;

    public DeleteMessageCommandChild(Notifiable childServer, Message messageToDelete) {
        this.messageToDelete = messageToDelete;
        this.childServer = childServer;
    }


    public void execute() throws RemoteException {
        if (childServer == null || messageToDelete == null) {
            return;
        }
        childServer.notifyDelete(messageToDelete);
    }

    @Override
    public void setNotifiable(Notifiable childServer) {
        this.childServer = childServer;
    }

    public ChildCommand clone() {
        return new DeleteMessageCommandChild(this.childServer,this.messageToDelete);
    }
}


