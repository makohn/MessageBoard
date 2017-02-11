package de.htwsaar.wirth.server.util.command.parent;

import java.rmi.RemoteException;

import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;

public class DeleteMessageCommandParent extends ParentCommand {

    private Message messageToDelete;
    private ParentServer parentToDelete;

    public DeleteMessageCommandParent(ParentServer parentToDelete, Message messageToDelete) {
        this.messageToDelete = messageToDelete;
        this.parentToDelete = parentToDelete;
    }

    /**
     * After a parent received this command it will be executed by the CommandRunner.
     * @throws RemoteException
     */
    public void execute() throws RemoteException {
        if (parentToDelete == null || messageToDelete == null) {
            return;
        }
        parentToDelete.notifyServerDelete(messageToDelete);
    }


}


