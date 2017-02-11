package de.htwsaar.wirth.server.util.command.parent;

import java.rmi.RemoteException;

import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;

public class EditMessageCommandParent extends ParentCommand {

    private Message messageToEdit;
    private ParentServer parentToEdit;

    public EditMessageCommandParent(ParentServer parentToEdit, Message message) {
        this.messageToEdit = message;
        this.parentToEdit = parentToEdit;
    }

    /**
     * After a parent received this command it will be executed by the CommandRunner.
     * @throws RemoteException
     */
    public void execute() throws RemoteException {
        if (parentToEdit == null || messageToEdit == null) {
            return;
        }
        parentToEdit.notifyServerEdit(messageToEdit);
    }


}


