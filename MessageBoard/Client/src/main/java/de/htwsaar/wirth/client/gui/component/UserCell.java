package de.htwsaar.wirth.client.gui.component;

import de.htwsaar.wirth.client.util.Status;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Pair;

public class UserCell extends ListCell<Pair<String,Status>> {
	
	private	final String STATUS_SYMBOL_FILLED = "\u25CF";
	private	final String STATUS_SYMBOL_EMPTY  = "\u25CB";
	
	private final Color GREEN 	= Color.rgb(124, 255, 25);
	private final Color RED 	= Color.rgb(244, 66, 66);
	private final Color YELLOW 	= Color.rgb(244, 226, 66);
	private final Color GREY	= Color.rgb(201, 201, 200);
	
	private HBox listEntry = new HBox(5);
	private Text username = new Text("");
	private Text status = new Text("");
	
	
	@Override
	protected void updateItem(Pair<String, Status> item, boolean empty) {
		super.updateItem(item, empty);
        setBackground(null);
        if (item != null) {

            username.setText(item.getKey());
            username.setFill(GREY);
            
            switch(item.getValue()) {
            case ONLINE:
                status.setText(STATUS_SYMBOL_FILLED);
            	status.setFill(GREEN);
            	break;
            case AWAY:
                status.setText(STATUS_SYMBOL_FILLED);
            	status.setFill(YELLOW);
            	break;
            case BUSY:
                status.setText(STATUS_SYMBOL_FILLED);
            	status.setFill(RED);
            	break;
            default:
            case SHOW_AS_OFFLINE:
                status.setText(STATUS_SYMBOL_EMPTY);
            	status.setFill(GREEN);
            	break;
            }

            listEntry.getChildren().addAll(status, username);
            listEntry.setAlignment(Pos.CENTER_LEFT);

            setGraphic(listEntry);
        }
    }
	
}
