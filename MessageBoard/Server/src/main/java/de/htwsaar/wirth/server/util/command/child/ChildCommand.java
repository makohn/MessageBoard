package de.htwsaar.wirth.server.util.command.child;

import de.htwsaar.wirth.remote.Notifiable;
import de.htwsaar.wirth.server.util.command.Command;


public abstract class ChildCommand implements Command{

    public abstract void setNotifiable(Notifiable n);
    public abstract ChildCommand clone();
}
