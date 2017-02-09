package de.htwsaar.wirth.server.util.CommandFactory.factories;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.CommandFactory.CommandModel.*;


public class EditMessageCommandFactory implements CommandFactory {



    public Command makeCommand(ParentServer server, Message msg) {
        Command command = new EditMessageCommandParent(server,msg);
        return command;
    }
    public Command makeCommand(Notifiable server, Message msg){
        Command command = new EditMessageCommandChild(server,msg);
        return command;
    }
}
