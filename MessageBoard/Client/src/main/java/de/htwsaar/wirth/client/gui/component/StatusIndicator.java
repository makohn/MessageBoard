package de.htwsaar.wirth.client.gui.component;

import de.htwsaar.wirth.client.util.UIConstants;
import de.htwsaar.wirth.remote.model.Status;
import javafx.scene.text.Text;

public class StatusIndicator extends Text {

	public static final String DEFAULT_USERNAME_STYLE = "username";
	
	public StatusIndicator() {
		this(Status.SHOW_AS_OFFLINE);
	}
	
	public StatusIndicator(Status status) {
		setStatus(status); 
	}
	
	public void setStatus(Status status) {
		switch(status) {
        case ONLINE:
            super.setText(UIConstants.STATUS_SYMBOL_FILLED);
            super.setFill(UIConstants.STATUS_GREEN);
        	break;
        case AWAY:
        	super.setText(UIConstants.STATUS_SYMBOL_FILLED);
        	super.setFill(UIConstants.STATUS_YELLOW);
        	break;
        case BUSY:
        	super.setText(UIConstants.STATUS_SYMBOL_FILLED);
        	super.setFill(UIConstants.STATUS_RED);
        	break;
        default:
        case SHOW_AS_OFFLINE:
        	super.setText(UIConstants.STATUS_SYMBOL_EMPTY);
        	super.setFill(UIConstants.STATUS_GREEN);
        	break;
        }
	}
}
