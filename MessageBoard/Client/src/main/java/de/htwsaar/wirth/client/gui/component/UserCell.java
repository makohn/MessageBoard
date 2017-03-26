package de.htwsaar.wirth.client.gui.component;

import java.util.Optional;

import org.controlsfx.glyphfont.Glyph;

import de.htwsaar.wirth.client.ClientImpl;
import de.htwsaar.wirth.client.controller.MainViewController;
import de.htwsaar.wirth.remote.model.Status;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Pair;

public class UserCell extends ListCell<Pair<String,Status>> {
	
	private HBox listEntry;
	private Text username;
	private StatusIndicator status;
	private MainViewController mainView;
	private Button delete = new Button();
	private ClientImpl client;
	
	private static final String BTN_STYLECLASS_DELETE = "btnDeleteUser";

    public UserCell (MainViewController mainView)
    {
        this.mainView = mainView;
        this.delete = new Button();
        this.client = ClientImpl.getInstance();
    }
    
	@Override
	protected void updateItem(Pair<String, Status> item, boolean empty) {
		super.updateItem(item, empty);
        setBackground(null);

        if (!empty && item != null) {
    		listEntry = new HBox(5);
    		listEntry.setPrefHeight(10);
    		username = new Text("");
    		status = new StatusIndicator();
    		
            if(client.isGroupLeader() ) {
                initDeleteButton(item.getKey());
            }

            username.setText(item.getKey());
            username.setFill(Color.WHITE);
            
            status.setStatus(item.getValue());

            listEntry.getChildren().addAll(status, username, delete);
            listEntry.setAlignment(Pos.CENTER_LEFT);

            setGraphic(listEntry);
        }
    }
    private void initDeleteButton(String username) {
        if (!client.getUsername().equals(username)) {
        	delete.setGraphic(new Glyph("FontAwesome", "TRASH"));
            delete.getStyleClass().add(BTN_STYLECLASS_DELETE);
            delete.setVisible(false);
            delete.setManaged(false);
            this.setOnMouseEntered(e -> {
            	delete.setVisible(true);
            	delete.setManaged(true);
            });
            this.setOnMouseExited(e -> {
            	delete.setVisible(false);
            	delete.setManaged(false);
            });

            delete.setOnMouseClicked((e) -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Benutzer Löschen");
                alert.setContentText("Möchten Sie diesen Benutzer dauerhaft löschen?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Task<Void> deleteTask = ClientImpl.getInstance().deleteUser(username);
                    mainView.getExecutorService().submit(deleteTask);
                }
            });
        }
    }
}
