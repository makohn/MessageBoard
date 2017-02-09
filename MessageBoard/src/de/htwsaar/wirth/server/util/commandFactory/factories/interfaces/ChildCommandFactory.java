package de.htwsaar.wirth.server.util.commandFactory.factories.interfaces;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Command;

public interface ChildCommandFactory {

    int NEW_COMMAND= 3;

    public Command makeCommand(Notifiable server, Message msg,int commandType);

}
