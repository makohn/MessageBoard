package de.htwsaar.wirth.client.controller;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import de.htwsaar.wirth.client.gui.component.MessageCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class MainViewController implements Initializable {

	@FXML private ListView<String> chatPane;
	@FXML private ListView<String> userList;
	@FXML private TextArea messageBox;
	
	
	private ObservableList<String> messages;
	private ObservableList<String> users;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		messages = FXCollections.observableArrayList();
		users = FXCollections.observableArrayList("Folz", "Weber");
		chatPane.setMouseTransparent( true );
		chatPane.setFocusTraversable( false );
		chatPane.setCellFactory(lv -> new MessageCell());
		chatPane.setItems(messages);
		
		userList.setCellFactory(lv -> new ListCell<String>() {
			@Override
            protected void updateItem(String user, boolean bln) {
                super.updateItem(user, bln);
                setGraphic(null);
                setText(null);
                setBackground(null);
                if (user != null) {
                    HBox hBox = new HBox();

                    Text name = new Text(user);
                    name.setFill(Color.rgb(201, 201, 200));
                    
                    Text status = new Text("•  ");
                    status.setFill(Color.rgb(124, 255, 25));

                    hBox.getChildren().addAll(status, name);
                    hBox.setAlignment(Pos.CENTER_LEFT);

                    setGraphic(hBox);
                }
            }
		});
		userList.setItems(users);
		
		/* Added to prevent the enter from adding a new line to inputMessageBox */
        messageBox.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                try {
                    sendMethod(ke);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ke.consume();
            }
        });
	}

	
    public void sendMethod(KeyEvent ke) throws IOException {
    	 if (ke.getCode().equals(KeyCode.ENTER)) {
        	messages.add(messageBox.getText());
        	messageBox.clear();
    	 }
    }
}

