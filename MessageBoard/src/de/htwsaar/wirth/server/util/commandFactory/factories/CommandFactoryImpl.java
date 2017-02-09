package de.htwsaar.wirth.server.util.commandFactory.factories;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.commandFactory.CommandBuilder;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Command;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.childCommand.DeleteMessageCommandChild;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.childCommand.EditMessageCommandChild;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.parentCommand.DeleteMessageCommandParent;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.parentCommand.EditMessageCommandParent;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.parentCommand.PublishMessageCommand;
import de.htwsaar.wirth.server.util.commandFactory.factories.interfaces.CommandFactory;


public class CommandFactoryImpl implements CommandFactory {


    public Command makeCommand(ParentServer server, Message msg, int commandType) {
        Command command = null;
        if (commandType == DELETE_COMMAND) {
            command = new DeleteMessageCommandParent(server, msg);
        } else if (commandType == EDIT_COMMAND) {
            command = new EditMessageCommandParent(server, msg);
        }

        return command;
    }

    public Command makeCommand(Notifiable server, Message msg, int commandType) {
        Command command = null;
        if (commandType == DELETE_COMMAND) {
            command = new DeleteMessageCommandChild(server, msg);
        } else if (commandType == EDIT_COMMAND) {
            command = new EditMessageCommandChild(server, msg);
        }

        return command;
    }
}
