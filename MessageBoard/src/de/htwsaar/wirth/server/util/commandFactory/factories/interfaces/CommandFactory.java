package de.htwsaar.wirth.server.util.commandFactory.factories.interfaces;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Command;

public interface CommandFactory {

    int DELETE_COMMAND = 1;
    int EDIT_COMMAND = 2;

    Command makeCommand(Notifiable server, Message msg, int commandType);

    Command makeCommand(ParentServer server, Message msg, int commandType);

}
