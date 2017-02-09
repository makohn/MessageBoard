package de.htwsaar.wirth.server.util.commandFactory;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Command;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Constants.ChildCmd;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Constants.Cmd;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Constants.ParentCmd;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.childCommand.ChildCommand;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.parentCommand.ParentCommand;
import de.htwsaar.wirth.server.util.commandFactory.factories.ChildCommandFactoryImpl;
import de.htwsaar.wirth.server.util.commandFactory.factories.CommandFactoryImpl;
import de.htwsaar.wirth.server.util.commandFactory.factories.ParentCommandFactoryImpl;
import de.htwsaar.wirth.server.util.commandFactory.factories.interfaces.ChildCommandFactory;
import de.htwsaar.wirth.server.util.commandFactory.factories.interfaces.CommandFactory;
import de.htwsaar.wirth.server.util.commandFactory.factories.interfaces.ParentCommandFactory;


public class CommandBuilder {
    private static Command command;

    public static Command buildCommand(ParentServer server, Message msg, Cmd commandType) {

        CommandFactory factory = new CommandFactoryImpl();
        command = factory.makeCommand(server, msg, commandType);

        return command;
    }

    public static Command buildCommand(Notifiable server, Message msg, Cmd commandType) {
        CommandFactory factory = new CommandFactoryImpl();
        command = factory.makeCommand(server, msg, commandType);

        return command;
    }
    public static ChildCommand buildCommand(Message msg, Cmd commandType) {
        CommandFactory factory = new CommandFactoryImpl();
        ChildCommand command = factory.makeCommand(msg, commandType);

        return command;
    }

    public static ChildCommand buildChildCommand(Notifiable server, Message msg, ChildCmd commandType) {
        ChildCommandFactory factory = new ChildCommandFactoryImpl();
        ChildCommand command = factory.makeCommand(server, msg, commandType);

        return command;
    }
    public static ChildCommand buildChildCommand( Message msg, ChildCmd commandType) {
        ChildCommandFactory factory = new ChildCommandFactoryImpl();
        ChildCommand command = factory.makeCommand( msg, commandType);

        return command;
    }

    public static ParentCommand buildParentCommand(ParentServer server, Message msg, ParentCmd commandType) {
        ParentCommandFactory factory = new ParentCommandFactoryImpl();
        ParentCommand command = factory.makeCommand(server, msg, commandType);

        return command;
    }

}
