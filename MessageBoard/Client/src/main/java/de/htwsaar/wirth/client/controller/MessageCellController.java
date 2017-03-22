package de.htwsaar.wirth.client.controller;

import java.text.DateFormat;

import de.htwsaar.wirth.client.ClientImpl;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Label;

public class MessageCellController {
	@FXML private Label usernameLabel;
	@FXML private Label groupLabel;
	@FXML private Label creationDateLabel;
	@FXML private Label modificationTextLabel;
	@FXML private Label modificationDateLabel;
	@FXML private Label messageArea;
	@FXML private TextField txtMessageEdit;
	@FXML private Button publishButton;
	@FXML private Button editButton;
	@FXML private Button deleteButton;
	
	private Message message;
	private MainViewController mainView;
	
	private boolean isEditMode = false;
	
	private ClientImpl client = ClientImpl.getInstance();


	public void initCell(MainViewController mainView, Message msg) {
		this.message = msg;
		this.mainView = mainView;
		
		// init view
		
		setUsername(message.getAuthor());
    	setGroup(message.getGroup());
    	setCreationDate(DateFormat.getDateTimeInstance().format(message.getCreatedAt()));
    	
    	// show modification date, if they are not equal
    	if (!message.getCreatedAt().equals(message.getModifiedAt())) {
    		setModificationDate(DateFormat.getDateTimeInstance().format(message.getModifiedAt()));
    	} else {
    		disableModificationDate();
    	}
    	
    	setMessageTxt(msg.getMessage());
    	
    	// disable or enable Buttons and register EventHandler
		
		boolean isAuthor = ClientImpl.getInstance().getUsername().equals(message.getAuthor()) 
							&& ClientImpl.getInstance().getGroupName().equals(message.getGroup());

		if (!isAuthor) {
			disableButton(editButton);
		} else {
			enableButton(editButton);
			initEditEventHandler();
		}
		if (!ClientImpl.getInstance().isGroupLeader()) {
			disableButton(publishButton);
		} else {
			enableButton(publishButton);
			initPublishEventHandler();
		}
		if (!(ClientImpl.getInstance().isGroupLeader() || isAuthor)) {
			disableButton(deleteButton);
		} else {
			enableButton(deleteButton);
			initDeleteEventHandler();
		}
	}
	
	private void initPublishEventHandler() {
		publishButton.setOnAction((actionEvent) -> {
			Task<Void> publishTask = client.publishMessage(message.getID());
			publishTask.setOnFailed((workerStateEvent) -> {
				mainView.onError(workerStateEvent.getSource().getException());
			});
			mainView.getExecutorService().submit(publishTask);
		});
		
	}

	private void initEditEventHandler() {
		editButton.setOnAction((actionEvent) -> {
			changeIntoEditMode();
			// Beenden des Edits durch Enter
			txtMessageEdit.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
	            if (ke.getCode().equals(KeyCode.ENTER)) {
	            	editMessage();
	            	ke.consume();
	            }
	        });
			// wenn focus verloren wird, wird die Nachricht nicht geändert
			txtMessageEdit.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					// Focus lost
					boolean focusLost = oldValue && !newValue;
					if(focusLost) {
						changeIntoNormalMode();
					}
				}
			});			
		});
	}
	
	private void initDeleteEventHandler() {
		deleteButton.setOnAction((actionEvent) -> {
			Task<Void> deleteTask = client.deleteMessage(message.getID());
			deleteTask.setOnFailed((workerStateEvent) -> {
				mainView.onError(workerStateEvent.getSource().getException());
			});
			mainView.getExecutorService().submit(deleteTask);
		});
	}
	
	private void editMessage() {
		if (isEditMode) {
			String editedMsgTxt = txtMessageEdit.getText();
			Task<Void> editTask = client.editMessage(editedMsgTxt, message.getID());
			editTask.setOnFailed((workerStateEvent) -> {
				mainView.onError(workerStateEvent.getSource().getException());
			});
			mainView.getExecutorService().submit(editTask);
			changeIntoNormalMode();
		}
	}
	
	private void changeIntoEditMode() {
		isEditMode = true;
		messageArea.setManaged(false);
		messageArea.setVisible(true);
		txtMessageEdit.setText(message.getMessage());
		txtMessageEdit.setManaged(true);
		txtMessageEdit.setVisible(true);
		txtMessageEdit.requestFocus();
	}

	private void changeIntoNormalMode() {
		messageArea.setManaged(true);
		messageArea.setVisible(true);
		txtMessageEdit.setManaged(false);
		txtMessageEdit.setVisible(false);
		isEditMode = false;
	}
	
	private void enableButton(Button btn) {
		btn.setManaged(true);
		btn.setVisible(true);
	}
	
	private void disableButton(Button btn) {
		btn.setVisible(false);
		btn.setManaged(false);
	}
	
	private void setUsername(String username) {
		usernameLabel.setText(username);
	}
	
	private void setGroup(String group) {
		groupLabel.setText(group);
	}
	
	private void setCreationDate(String date) {
		creationDateLabel.setText(date);
	}
	
	private void setModificationDate(String date) {
		modificationDateLabel.setText(date);
		modificationTextLabel.setVisible(true);
		modificationDateLabel.setVisible(true);
	}
	
	private void setMessageTxt(String msg) {
    	messageArea.setText(msg);
    }

	private void disableModificationDate() {
		modificationTextLabel.setVisible(false);
		modificationDateLabel.setVisible(false);
	}
}
