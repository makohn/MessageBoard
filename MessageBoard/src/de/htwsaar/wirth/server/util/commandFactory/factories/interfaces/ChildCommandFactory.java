package de.htwsaar.wirth.server.util.commandFactory.factories.interfaces;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Command;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Constants.ChildCmd;

public interface ChildCommandFactory {



    Command makeCommand(Notifiable server, Message msg, ChildCmd commandType);

}
