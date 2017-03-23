package de.htwsaar.wirth.client.controller;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ResourceBundle;

import de.htwsaar.wirth.client.ClientImpl;
import de.htwsaar.wirth.client.gui.ApplicationDelegate;
import de.htwsaar.wirth.client.gui.component.MessageCell;
import de.htwsaar.wirth.client.gui.component.UserCell;
import de.htwsaar.wirth.remote.model.Status;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class MainViewController implements Initializable {

	@FXML private Button groupButton;
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
	
    private ExecutorService exec;
	
	@SuppressWarnings("unchecked") // Arraylist does not like Generics
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		exec = Executors.newCachedThreadPool();
		
		client = ClientImpl.getInstance();
		client.setView(this);
		
		groupButton.setText(client.getGroupName());
		usernameLabel.setText(client.getUsername());
		fullNameLabel.setText(client.getUsername());

		// Messages
		messages = FXCollections.observableArrayList();
		sortedWrapperList = messages.sorted((m1, m2) -> {
			return m1.getCreatedAt().compareTo(m2.getCreatedAt());
		});
		chatPane.setSelectionModel(new NoSelectionModel());
		chatPane.setCellFactory(list -> new MessageCell(this));
		chatPane.setItems(sortedWrapperList);

		// Users
		users = FXCollections.observableArrayList();		
		userList.setCellFactory(list -> new UserCell());
		userList.setItems(users);
		userList.setPrefHeight(users.size() * 28);
		
		// Groups
		groups = FXCollections.observableArrayList("ALL");
		groups.addListener((ListChangeListener<String>) c -> {
            if (groupList != null)
                groupList.setPrefHeight(groups.size() * 28);
        });
		groupList.setItems(groups);
		groupList.setPrefHeight(groups.size() * 28);
		
		refreshAllMessages(true);
		refreshAllUserStatus();
		
		initSendMessageButton();
	}



	private void initSendMessageButton() {
		/* Added to prevent the enter from adding a new line to inputMessageBox */
        messageBox.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
            	sendMessage();
            	ke.consume();
            }
        });
	}


	
	private void refreshAllMessages(boolean shouldScrollToLast) {
		Task<List<Message>> messageTask = client.getAllMessages();
		messageTask.setOnSucceeded((e) -> {
			List<Message> messageList = messageTask.getValue();
			messages.clear();
			for (Message msg : messageList) {
				insertMessage(msg);
			}
			if (shouldScrollToLast)
				scrollToLastMessage();
		});
		messageTask.setOnFailed((e) -> {
			onError(e.getSource().getException());
		});
		exec.submit(messageTask);
	}
	
	public void onError(Throwable e) {
		// TODO:
		e.printStackTrace();
		Task<Void> logoutTask = ClientImpl.getInstance().logout();
		exec.submit(logoutTask);
		ApplicationDelegate.getInstance().showLoginScreen();
	}

	private void refreshAllUserStatus() {
		Task<Map<String, Status>> getUserStatusTask = client.getUserStatus();
		getUserStatusTask.setOnSucceeded((e) -> {
			Map<String, Status> userStatusMap = getUserStatusTask.getValue();
			for (Entry<String, Status> entry : userStatusMap.entrySet()) {
				users.add(new Pair<String, Status> (entry.getKey(), entry.getValue()));
			}
		});
		getUserStatusTask.setOnFailed((e) -> {
			onError(e.getSource().getException());
		});
		exec.submit(getUserStatusTask);
	}
	
	private void scrollToLastMessage() {
		if (!chatPane.getItems().isEmpty())
			chatPane.scrollTo(chatPane.getItems().size() - 1);
	}

	public void insertMessage(Message msg) {
		messages.remove(msg);
		messages.add(msg);
		
		if (!groups.contains(msg.getGroup())) {
			groups.add(msg.getGroup());
		}
	}
	
	public void editMessage(Message msg) {
		messages.remove(msg);
		messages.add(msg);
	}
	
	public void deleteMessage(Message msg) {
		messages.remove(msg);
	}
	
	public void changeUserStatus(String username, Status status) {
		users.removeIf((pair) -> username.equals(pair.getKey()));
		users.add(new Pair<String, Status>(username, status));
	}
	
	public void deleteUser(String username) {
		users.removeIf((pair) -> username.equals(pair.getKey()));
	}

    public void sendMessage() {
    	 if (!messageBox.getText().isEmpty()) {
        	Task<Void> sendMessageTask = client.sendMessage(messageBox.getText());
        	sendMessageTask.setOnFailed((e) -> {
        		onError(e.getSource().getException());
        	});
        	exec.submit(sendMessageTask);
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

	public ExecutorService getExecutorService() {
		return exec;
	}
}

