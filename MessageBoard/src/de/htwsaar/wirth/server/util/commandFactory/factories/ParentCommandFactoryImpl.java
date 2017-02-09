package de.htwsaar.wirth.server.util.commandFactory.factories;

import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.commandFactory.CommandBuilder;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Command;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.parentCommand.PublishMessageCommand;
import de.htwsaar.wirth.server.util.commandFactory.factories.interfaces.ParentCommandFactory;


public class ParentCommandFactoryImpl implements ParentCommandFactory {




    public Command makeCommand(ParentServer server, Message msg,int commandType){
        Command command = null;
        if(commandType == PUBLISH_COMMAND)
        {
            command = new PublishMessageCommand(server,msg);
        }

        return command;
    }
}
