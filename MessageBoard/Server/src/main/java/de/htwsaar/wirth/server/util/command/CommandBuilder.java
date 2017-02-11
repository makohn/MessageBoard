package de.htwsaar.wirth.server.util.command;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.command.child.ChildCommand;
import de.htwsaar.wirth.server.util.command.parent.ParentCommand;


public class CommandBuilder {



    /**
     * buildChildCommand creates a Command that is only for ChildServer
     *
     * @param server      ParentServer
     * @param msg         a Message
     * @param commandType a commandType specified in ENUM Cmd
     * @return
     */
    public static ChildCommand buildChildCommand(Notifiable server, Message msg, ChildCmd commandType) {
        ChildCommandFactory factory = new ChildCommandFactoryImpl();
        ChildCommand command = factory.makeCommand(server, msg, commandType);

        return command;
    }

    /**
     * buildChildCommand creates a Command that is only for ChildServer. The server isn't set yet.
     *
     * @param msg
     * @param commandType
     * @return
     */
    public static ChildCommand buildChildCommand(Message msg, ChildCmd commandType) {
        ChildCommandFactory factory = new ChildCommandFactoryImpl();
        ChildCommand command = factory.makeCommand(null, msg, commandType);

        return command;
    }

    /**
     * buildChildCommand creates a Command that is only for ParentServer.
     *
     * @param server      ParentServer
     * @param msg         a Message
     * @param commandType a commandType specified in ENUM Cmd
     * @return
     */
    public static ParentCommand buildParentCommand(ParentServer server, Message msg, ParentCmd commandType) {
        ParentCommandFactory factory = new ParentCommandFactoryImpl();
        ParentCommand command = factory.makeCommand(server, msg, commandType);

        return command;
    }

}
