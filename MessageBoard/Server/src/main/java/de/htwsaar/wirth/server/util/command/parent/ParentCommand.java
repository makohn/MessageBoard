package de.htwsaar.wirth.server.util.command.parent;

import de.htwsaar.wirth.remote.ParentServer;
import de.htwsaar.wirth.server.util.command.Command;


public abstract class ParentCommand implements Command{
	
	protected ParentServer parentToNotify;
	
    public void setNewParent(ParentServer newParentServer) {
    	this.parentToNotify = newParentServer;
    }
}
