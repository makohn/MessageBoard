package de.htwsaar.wirth.server.util.commandFactory.factories.interfaces;

import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Command;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Constants.ParentCmd;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.parentCommand.ParentCommand;

public interface ParentCommandFactory {



    ParentCommand makeCommand(ParentServer server, Message msg, ParentCmd commandType);


}
