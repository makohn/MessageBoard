package de.htwsaar.wirth.client.controller;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

import de.htwsaar.wirth.client.ClientImpl;
import de.htwsaar.wirth.client.gui.ApplicationDelegate;
import de.htwsaar.wirth.client.gui.component.MessageCell;
import de.htwsaar.wirth.client.gui.component.UserCell;
import de.htwsaar.wirth.client.util.UIConstants;
import de.htwsaar.wirth.remote.model.Status;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
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
	@FXML private ComboBox<Status> cmbStatus;
	@FXML private Label lblOwnStatus;
	@FXML private ToggleButton toggleUserList;
	@FXML private ToggleButton toggleGroupList;
	@FXML private VBox userArea;
	@FXML private VBox groupArea;
	@FXML private Button btnAllFilter;
	
	private ObservableList<Message> messages;
	private FilteredList<Message> filteredAndSortedList;
	private ObservableList<String> groups;
	private ObservableList<Pair<String,Status>> users;
	
	private ClientImpl client;
	
    private ExecutorService exec;
    
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
		ObservableList<Message> sortedWrapperList = messages.sorted((m1, m2) -> {
			return m1.getCreatedAt().compareTo(m2.getCreatedAt());
		});
		filteredAndSortedList = new FilteredList<Message>(sortedWrapperList);
		chatPane.setSelectionModel(new NoSelectionModel());
		chatPane.setCellFactory(list -> new MessageCell(this));
		chatPane.setItems(filteredAndSortedList);

		// Users
		users = FXCollections.observableArrayList();		
		userList.setCellFactory(list -> new UserCell());
		userList.setItems(users);
		
		// Groups
		groups = FXCollections.observableArrayList();
		groupList.setItems(groups);
		
		// Status
		cmbStatus.setItems(FXCollections.observableArrayList(Status.values()));
		lblOwnStatus.setText(UIConstants.STATUS_SYMBOL_FILLED);
		cmbStatus.valueProperty().addListener(new ChangeListener<Status>() {
			@Override
			public void changed(ObservableValue<? extends Status> observable, Status oldValue, Status newValue) {
				lblOwnStatus.setTextFill(newValue.getColor());
				Task<Void> changeStatusTask = client.changeUserStatus(newValue);
				changeStatusTask.setOnFailed(e -> {
					onError(e.getSource().getException());
				});
				exec.submit(changeStatusTask);
			}
	    });
		
		refreshAllMessages(true);
		refreshAllUserStatus();
		
		initSendMessageButton();
		initAllFilterButton();
		initGroupFilter();
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
	
	private void initAllFilterButton() {
		btnAllFilter.setOnAction(e -> {
			// setPredicate(null), entfernt jegliche Filterung
			filteredAndSortedList.setPredicate(null);
			// entferne die Selektierung, damit wir das Selektieren eines bereits vorher selektierten Items
			// mitbekommen
			groupList.getSelectionModel().clearSelection();
		});
	}
	
	private void initGroupFilter() {
		groupList.getSelectionModel().selectedItemProperty().addListener((observable, oldVal, newVal) -> {
			// ignoriere deselektieren
			if (newVal == null) {
				return;
			}
			filteredAndSortedList.setPredicate(msg -> newVal.equals(msg.getGroup()));
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
		// TODO: Fehlerbehandlung (Dialog)
		e.printStackTrace();
		Task<Void> logoutTask = ClientImpl.getInstance().logout();
		exec.submit(logoutTask);
		ApplicationDelegate.getInstance().showLoginScreen();
	}

	private void refreshAllUserStatus() {
		Task<Map<String, Status>> getUserStatusTask = client.getUserStatus();
		getUserStatusTask.setOnSucceeded((e) -> {
			Map<String, Status> userStatusMap = getUserStatusTask.getValue();
			users.clear();
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
    
	public ExecutorService getExecutorService() {
		return exec;
	}
}

