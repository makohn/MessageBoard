package de.htwsaar.wirth.server.util.CommandFactory;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.CommandFactory.CommandModel.Command;
import de.htwsaar.wirth.server.util.CommandFactory.CommandModel.NewMessageCommand;
import de.htwsaar.wirth.server.util.CommandFactory.CommandModel.PublishMessageCommand;
import de.htwsaar.wirth.server.util.CommandFactory.factories.CommandFactory;
import de.htwsaar.wirth.server.util.CommandFactory.factories.DeleteMessageCommandFactory;
import de.htwsaar.wirth.server.util.CommandFactory.factories.EditMessageCommandFactory;


public class CommandBuilder {
    public static final int DELETE_COMMAND= 1;
    public static final int EDIT_COMMAND= 2;
    public static final int NEW_COMMAND= 3;
    public static final int PUBLISH_COMMAND= 3;




    public static Command buildCommand(ParentServer server, Message msg, int commandType) {
        Command command  = null;
        if(commandType == DELETE_COMMAND)
        {
            CommandFactory factory = new DeleteMessageCommandFactory();
            command = factory.makeCommand(server,msg);
        }else if(commandType == EDIT_COMMAND)
        {
            CommandFactory factory = new EditMessageCommandFactory();
            command = factory.makeCommand(server,msg);
        }else if(commandType == PUBLISH_COMMAND)
        {
            command = new PublishMessageCommand(server,msg);
        }
        return command;
    }
    public static Command buildCommand( Notifiable server,Message msg, int commandType) {
        Command command  = null;
        if(commandType == DELETE_COMMAND)
        {
            CommandFactory factory = new DeleteMessageCommandFactory();
            command = factory.makeCommand(server,msg);
        }
        else if(commandType == EDIT_COMMAND)
        {
            CommandFactory factory = new EditMessageCommandFactory();
            command = factory.makeCommand(server,msg);
        }else if(commandType == NEW_COMMAND)
        {
            command = new NewMessageCommand(server,msg);
        }
        return command;
    }

}
