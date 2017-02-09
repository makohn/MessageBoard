package de.htwsaar.wirth.server.util.CommandFactory.factories;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.CommandFactory.CommandModel.Command;

public interface CommandFactory{

    public Command makeCommand(ParentServer server, Message msg);
    public Command makeCommand(Notifiable server, Message msg);

}
