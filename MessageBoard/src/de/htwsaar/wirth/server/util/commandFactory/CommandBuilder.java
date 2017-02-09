package de.htwsaar.wirth.server.util.commandFactory;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Command;
import de.htwsaar.wirth.server.util.commandFactory.factories.ChildCommandFactoryImpl;
import de.htwsaar.wirth.server.util.commandFactory.factories.CommandFactoryImpl;
import de.htwsaar.wirth.server.util.commandFactory.factories.ParentCommandFactoryImpl;
import de.htwsaar.wirth.server.util.commandFactory.factories.interfaces.ChildCommandFactory;
import de.htwsaar.wirth.server.util.commandFactory.factories.interfaces.CommandFactory;
import de.htwsaar.wirth.server.util.commandFactory.factories.interfaces.ParentCommandFactory;


public class CommandBuilder {


    public static Command buildCommand(ParentServer server, Message msg, int commandType) {

        CommandFactory factory = new CommandFactoryImpl();
        Command command = factory.makeCommand(server,msg,commandType);

        return command;
    }

    public static Command buildCommand(Notifiable server,Message msg, int commandType) {
        CommandFactory factory = new CommandFactoryImpl();
        Command command = factory.makeCommand(server,msg,commandType);

        return command;
    }

    public static Command buildChildCommand( Notifiable server,Message msg, int commandType) {
        ChildCommandFactory factory = new ChildCommandFactoryImpl();
        Command command = factory.makeCommand(server,msg,commandType);

        return command;
    }

    public static Command buildParentCommand( ParentServer server,Message msg, int commandType) {
        ParentCommandFactory factory = new ParentCommandFactoryImpl();
        Command command = factory.makeCommand(server,msg,commandType);

        return command;
    }

}
