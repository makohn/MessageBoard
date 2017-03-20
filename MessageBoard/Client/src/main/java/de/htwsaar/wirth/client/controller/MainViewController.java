package de.htwsaar.wirth.client.controller;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import de.htwsaar.wirth.client.ClientImpl;
import de.htwsaar.wirth.client.gui.component.MessageCell;
import de.htwsaar.wirth.client.gui.component.UserCell;
import de.htwsaar.wirth.remote.model.Status;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class MainViewController implements Initializable {

	@FXML private ListView<Message> chatPane;
	@FXML private ListView<String> groupList;
	@FXML private ListView<Pair<String,Status>> userList;
	@FXML private TextArea messageBox;
	@FXML private Label	reloadLabel;
	@FXML private Label usernameLabel;
	@FXML private Label fullNameLabel;
	@FXML private ToggleButton toggleUserList;
	@FXML private ToggleButton toggleGroupList;
	@FXML private VBox userArea;
	@FXML private VBox groupArea;
	
	
	private ObservableList<Message> messages;
	private ObservableList<Message> sortedWrapperList;
	private ObservableList<String> groups;
	private ObservableList<Pair<String,Status>> users;
	
	private ClientImpl client;
	
	@SuppressWarnings("unchecked") // Arraylist does not like Generics
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		client = ClientImpl.getInstance();
		client.setView(this);
		usernameLabel.setText(client.getUsername());
		// TODO: add fullName here
		fullNameLabel.setText(client.getUsername());

		messages = FXCollections.observableArrayList();
		sortedWrapperList = messages.sorted((m1, m2) -> { 
			return m1.getCreatedAt().compareTo(m2.getCreatedAt());
		});		
		chatPane.setCellFactory(list -> new MessageCell());
		chatPane.setItems(sortedWrapperList);

		users = FXCollections.observableArrayList();		
		userList.setCellFactory(list -> new UserCell());
		initUserStatus();
		userList.setItems(users);
		userList.setPrefHeight(users.size() * 28);
		
		groups = FXCollections.observableArrayList("ALL");
		groups.addListener(new ListChangeListener<String>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends String> c) {
				if (groupList != null)
					groupList.setPrefHeight(groups.size() * 28);
			}
		});
		groupList.setItems(groups);
		groupList.setPrefHeight(groups.size() * 28);
		
		initMessages();
		
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
	
	private void initMessages() {
		try {
			for(Message msg : client.getAllMessages()) {
				insertMessage(msg);
			}
		} catch (RemoteException e) {}
	}

	private void initUserStatus() {
		try {
			Map<String, Status> userStatus = client.getUserStatus();
			for (Entry<String, Status> entry : userStatus.entrySet()) {
				users.add(new Pair<String, Status> (entry.getKey(), entry.getValue()));
			}
		} catch (RemoteException e) {
		}
	}

	public void insertMessage(Message msg) {
		if (!groups.contains(msg.getGroup())) {
			groups.add(msg.getGroup());
		}
		messages.add(msg);
	}
	
	public void editMessage(Message msg) {
		messages.remove(msg);
		messages.add(msg);
	}
	
	public void deleteMessage(Message msg) {
		messages.remove(msg);
	}
	
	public void changeUserStatus(String username, Status status) {
		users.removeIf((pair) -> {
			return username.equals(pair.getKey());
		});
		users.add(new Pair<String, Status>(username, status));
	}
	
	public void deleteUser(String username) {
		users.removeIf((pair) -> {
			return username.equals(pair.getKey());
		});
	}

    public void sendMethod(KeyEvent ke) throws RemoteException {
    	 if (ke.getCode().equals(KeyCode.ENTER)) {
        	client.sendMessage(messageBox.getText());
        	messageBox.clear();
    	 }
    }
    
    public void hideUserList() {
    	if(toggleUserList.isSelected()) {
    		userArea.getChildren().remove(userList);
    	} else {
    		userArea.getChildren().add(userList);
    	}
    }
    
    public void hideGroupList() {
    	if(toggleGroupList.isSelected()) {
    		groupArea.getChildren().remove(groupList);
    	} else {
    		groupArea.getChildren().add(groupList);
    	}
    }
}

