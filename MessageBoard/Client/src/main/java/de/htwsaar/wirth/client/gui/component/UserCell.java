package de.htwsaar.wirth.client.gui.component;

import de.htwsaar.wirth.remote.model.Status;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Pair;

public class UserCell extends ListCell<Pair<String,Status>> {
	
	private HBox listEntry;
	private Text username;
	private StatusIndicator status;
	
	// ------------------------------
	{
		listEntry = new HBox(5);
		username = new Text("");
		status = new StatusIndicator();
	}
	// ------------------------------
	
	@Override
	protected void updateItem(Pair<String, Status> item, boolean empty) {
		super.updateItem(item, empty);
        setBackground(null);
        if (item != null) {

            username.setText(item.getKey());
            username.setFill(Color.WHITE);
            
            status.setStatus(item.getValue());

            listEntry.getChildren().addAll(status, username);
            listEntry.setAlignment(Pos.CENTER_LEFT);

            setGraphic(listEntry);
        }
    }
	
}
