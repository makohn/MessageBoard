package de.htwsaar.wirth.server.util.commandFactory.factories;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.util.commandFactory.CommandBuilder;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Command;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.childCommand.NewMessageCommand;
import de.htwsaar.wirth.server.util.commandFactory.factories.interfaces.ChildCommandFactory;


public class ChildCommandFactoryImpl implements ChildCommandFactory {


    public Command makeCommand(Notifiable server, Message msg, int commandType) {
        Command command = null;
        if (commandType == NEW_COMMAND) {
            command = new NewMessageCommand(server, msg);
        }

        return command;
    }
}
