package de.htwsaar.wirth.server.util.command;

import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.command.parent.ParentCommand;

public interface ParentCommandFactory {



    ParentCommand makeCommand(ParentServer server, Message msg, ParentCmd commandType);


}
