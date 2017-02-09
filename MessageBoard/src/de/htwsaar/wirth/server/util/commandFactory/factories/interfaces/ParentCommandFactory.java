package de.htwsaar.wirth.server.util.commandFactory.factories.interfaces;

import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Command;

public interface ParentCommandFactory {

    int PUBLISH_COMMAND = 4;

    Command makeCommand(ParentServer server, Message msg, int commandType);


}
