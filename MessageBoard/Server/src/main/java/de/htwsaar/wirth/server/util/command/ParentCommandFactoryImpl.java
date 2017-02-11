package de.htwsaar.wirth.server.util.command;

import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.command.parent.DeleteMessageCommandParent;
import de.htwsaar.wirth.server.util.command.parent.EditMessageCommandParent;
import de.htwsaar.wirth.server.util.command.parent.ParentCommand;
import de.htwsaar.wirth.server.util.command.parent.PublishMessageCommand;


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
