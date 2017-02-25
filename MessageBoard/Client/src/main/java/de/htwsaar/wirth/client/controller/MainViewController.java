package de.htwsaar.wirth.client.controller;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import de.htwsaar.wirth.client.gui.component.MessageCell;
import de.htwsaar.wirth.client.gui.component.UserCell;
import de.htwsaar.wirth.client.util.Status;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Pair;

public class MainViewController implements Initializable {

	@FXML private ListView<String> chatPane;
	@FXML private ListView<Pair<String,Status>> userList;
	@FXML private TextArea messageBox;
	
	
	private ObservableList<String> messages;
	private ObservableList<Pair<String,Status>> users;
	
	///*-----------Example: Remove as soon as real datasets are available-----------------------------
	private final Pair<String,Status> user1 = new Pair<String,Status>("Folz", Status.AWAY);
	private final Pair<String,Status> user2 = new Pair<String,Status>("Weber", Status.ONLINE);
	private final Pair<String,Status> user3 = new Pair<String,Status>("Miede", Status.SHOW_AS_OFFLINE);
	private final Pair<String,Status> user4 = new Pair<String,Status>("Esch", Status.BUSY);
	///*----------------------------------------------------------------------------------------------
	
	@SuppressWarnings("unchecked") // Arraylist does not like Generics
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		messages = FXCollections.observableArrayList();
		chatPane.setCellFactory(list -> new MessageCell());
		chatPane.setItems(messages);
		
		users = FXCollections.observableArrayList(user1, user2, user3, user4);
		userList.setCellFactory(list -> new UserCell());	
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

