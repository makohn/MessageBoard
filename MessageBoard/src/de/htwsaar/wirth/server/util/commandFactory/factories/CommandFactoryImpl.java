package de.htwsaar.wirth.server.util.commandFactory.factories;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.commandFactory.CommandBuilder;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Command;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Constants.Cmd;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.childCommand.ChildCommand;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.childCommand.DeleteMessageCommandChild;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.childCommand.EditMessageCommandChild;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.parentCommand.DeleteMessageCommandParent;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.parentCommand.EditMessageCommandParent;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.parentCommand.ParentCommand;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.parentCommand.PublishMessageCommand;
import de.htwsaar.wirth.server.util.commandFactory.factories.interfaces.CommandFactory;


public class CommandFactoryImpl implements CommandFactory {


    public ParentCommand makeCommand(ParentServer server, Message msg, Cmd commandType) {
        ParentCommand command = null;
        if (commandType == Cmd.DELETE) {
            command = new DeleteMessageCommandParent(server, msg);
        } else if (commandType == Cmd.EDIT) {
            command = new EditMessageCommandParent(server, msg);
        }

        return command;
    }


    public ChildCommand makeCommand(Notifiable server, Message msg, Cmd commandType) {
        ChildCommand command = null;
        if (commandType == Cmd.DELETE) {
            command = new DeleteMessageCommandChild(server, msg);
        } else if (commandType == Cmd.EDIT) {

            command = new EditMessageCommandChild(server, msg);
        }

        return command;
    }
    public ChildCommand makeCommand( Message msg, Cmd commandType) {
        ChildCommand command = null;
        if (commandType == Cmd.DELETE) {
            command = new DeleteMessageCommandChild(null, msg);
        } else if (commandType == Cmd.EDIT) {

            command = new EditMessageCommandChild(null, msg);
        }

        return command;
    }
}
