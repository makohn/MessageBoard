package de.htwsaar.wirth.server.util.commandFactory.factories.interfaces;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Command;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Constants.Cmd;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.childCommand.ChildCommand;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.parentCommand.ParentCommand;

public interface CommandFactory {



    ChildCommand makeCommand(Notifiable server, Message msg, Cmd commandType);

    ChildCommand makeCommand(Message msg, Cmd commandType);

    ParentCommand makeCommand(ParentServer server, Message msg, Cmd commandType);

}
