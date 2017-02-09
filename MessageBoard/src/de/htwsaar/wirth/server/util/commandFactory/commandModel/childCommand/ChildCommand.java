package de.htwsaar.wirth.server.util.commandFactory.commandModel.childCommand;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.server.util.commandFactory.commandModel.Command;


public abstract class ChildCommand implements Command{

    public abstract void setNotifiable(Notifiable n);
}
