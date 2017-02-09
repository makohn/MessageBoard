package de.htwsaar.wirth.server.util.CommandFactory.factories;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.CommandFactory.CommandModel.Command;
import de.htwsaar.wirth.server.util.CommandFactory.CommandModel.DeleteMessageCommandChild;
import de.htwsaar.wirth.server.util.CommandFactory.CommandModel.DeleteMessageCommandParent;


public class DeleteMessageCommandFactory implements CommandFactory {



    public Command makeCommand(ParentServer server, Message msg) {
        Command command = new DeleteMessageCommandParent(server,msg);
        return command;
    }
    public Command makeCommand(Notifiable server, Message msg){
        Command command = new DeleteMessageCommandChild(server,msg);
        return command;
    }
}
