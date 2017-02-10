package de.htwsaar.wirth.server.util.commandFactory.factories;

import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Constants.ParentCmd;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.parentCommand.DeleteMessageCommandParent;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.parentCommand.EditMessageCommandParent;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.parentCommand.ParentCommand;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.parentCommand.PublishMessageCommand;
import de.htwsaar.wirth.server.util.commandFactory.factories.interfaces.ParentCommandFactory;


public class ParentCommandFactoryImpl implements ParentCommandFactory {


    public ParentCommand makeCommand(ParentServer server, Message msg, ParentCmd commandType) {
        ParentCommand command = null;
        if (commandType == ParentCmd.PUBLISH) {
            command = new PublishMessageCommand(server, msg);
        }else if (commandType == ParentCmd.DELETE) {
            command = new DeleteMessageCommandParent(server, msg);
        } else if (commandType == ParentCmd.EDIT) {
            command = new EditMessageCommandParent(server, msg);
        }

        return command;
    }
}
