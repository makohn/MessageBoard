package de.htwsaar.wirth.server.util.commandFactory.factories.interfaces;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Command;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Constants.Cmd;

public interface CommandFactory {



    Command makeCommand(Notifiable server, Message msg, Cmd commandType);

    Command makeCommand(ParentServer server, Message msg, Cmd commandType);

}
