package de.htwsaar.wirth.server.util.command;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.command.child.ChildCommand;

public interface ChildCommandFactory {



    ChildCommand makeCommand(Notifiable server, Message msg, ChildCmd commandType);
    ChildCommand makeCommand( Message msg, ChildCmd commandType);

}
