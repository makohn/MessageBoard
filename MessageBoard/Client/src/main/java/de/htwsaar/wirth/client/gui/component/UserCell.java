package de.htwsaar.wirth.client.gui.component;

import de.htwsaar.wirth.client.util.UIConstants;
import de.htwsaar.wirth.client.util.Status;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Pair;

public class UserCell extends ListCell<Pair<String,Status>> {
	
	private HBox listEntry;
	private Text username;
	private Text status;
	
	// ------------------------------
	{
		listEntry = new HBox(5);
		username = new Text("");
		status = new Text("");
	}
	// ------------------------------
	
	@Override
	protected void updateItem(Pair<String, Status> item, boolean empty) {
		super.updateItem(item, empty);
        setBackground(null);
        if (item != null) {

            username.setText(item.getKey());
            username.setFill(UIConstants.USERNAME_GREY);
            
            switch(item.getValue()) {
            case ONLINE:
                status.setText(UIConstants.STATUS_SYMBOL_FILLED);
            	status.setFill(UIConstants.STATUS_GREEN);
            	break;
            case AWAY:
                status.setText(UIConstants.STATUS_SYMBOL_FILLED);
            	status.setFill(UIConstants.STATUS_YELLOW);
            	break;
            case BUSY:
                status.setText(UIConstants.STATUS_SYMBOL_FILLED);
            	status.setFill(UIConstants.STATUS_RED);
            	break;
            default:
            case SHOW_AS_OFFLINE:
                status.setText(UIConstants.STATUS_SYMBOL_EMPTY);
            	status.setFill(UIConstants.STATUS_GREEN);
            	break;
            }

            listEntry.getChildren().addAll(status, username);
            listEntry.setAlignment(Pos.CENTER_LEFT);

            setGraphic(listEntry);
        }
    }
	
}
