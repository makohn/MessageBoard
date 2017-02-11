package de.htwsaar.wirth.server.util.command;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.command.child.ChildCommand;
import de.htwsaar.wirth.server.util.command.child.DeleteMessageCommandChild;
import de.htwsaar.wirth.server.util.command.child.EditMessageCommandChild;
import de.htwsaar.wirth.server.util.command.child.NewMessageCommand;


public class ChildCommandFactoryImpl implements ChildCommandFactory {


    public ChildCommand makeCommand(Notifiable server, Message msg, ChildCmd commandType) {
        ChildCommand command = null;
        if (commandType == ChildCmd.NEW) {
            command = new NewMessageCommand(server, msg);
        }else if (commandType == ChildCmd.DELETE) {
            command = new DeleteMessageCommandChild(server, msg);
        } else if (commandType == ChildCmd.EDIT) {
            command = new EditMessageCommandChild(server, msg);
        }

        return command;
    }

    public ChildCommand makeCommand( Message msg, ChildCmd commandType) {
        ChildCommand command = null;
        if (commandType == ChildCmd.DELETE) {
            command = new DeleteMessageCommandChild(null, msg);
        } else if (commandType == ChildCmd.EDIT) {

            command = new EditMessageCommandChild(null, msg);
        }
        return command;
    }

}
