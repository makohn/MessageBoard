package de.htwsaar.wirth.client.controller;

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
	@FXML private Button trashButton;
	
	private boolean isEditMode = false;
	
	private ClientImpl client = ClientImpl.getInstance();


	public void initEventHandler(MainViewController mainView, Message msg) {
		initPublishEventHandler(mainView, msg);
		initEditEventHandler(mainView, msg);
		initDeleteEventHandler(mainView, msg);
	}
	
	private void initPublishEventHandler(MainViewController mainView, Message msg) {
		publishButton.setOnAction((actionEvent) -> {
			Task<Void> publishTask = client.publishMessage(msg.getID());
			publishTask.setOnFailed((workerStateEvent) -> {
				mainView.onError(workerStateEvent.getSource().getException());
			});
			mainView.getExecutorService().submit(publishTask);
		});
		
	}

	private void initEditEventHandler(MainViewController mainView, Message msg) {
		editButton.setOnAction((actionEvent) -> {
			changeIntoEditMode(msg);
			// Beenden des Edits durch Enter
			txtMessageEdit.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
	            if (ke.getCode().equals(KeyCode.ENTER)) {
	            	editMessage(mainView, msg);
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
	
	private void editMessage(MainViewController mainView, Message msg) {
		if (isEditMode) {
			String editedMsgTxt = txtMessageEdit.getText();
			Task<Void> editTask = client.editMessage(editedMsgTxt, msg.getID());
			editTask.setOnFailed((workerStateEvent) -> {
				mainView.onError(workerStateEvent.getSource().getException());
			});
			mainView.getExecutorService().submit(editTask);
			changeIntoNormalMode();
		}
	}
	
	private void changeIntoEditMode(Message msg) {
		isEditMode = true;
		messageArea.setManaged(false);
		messageArea.setVisible(true);
		txtMessageEdit.setText(msg.getMessage());
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

	private void initDeleteEventHandler(MainViewController mainView, Message msg) {
		trashButton.setOnAction((actionEvent) -> {
			Task<Void> deleteTask = client.deleteMessage(msg.getID());
			deleteTask.setOnFailed((workerStateEvent) -> {
				mainView.onError(workerStateEvent.getSource().getException());
			});
			mainView.getExecutorService().submit(deleteTask);
		});
	}
	
	public void setUsername(String username) {
		usernameLabel.setText(username);
	}
	
	public void setGroup(String group) {
		groupLabel.setText(group);
	}
	
	public void setCreationDate(String date) {
		creationDateLabel.setText(date);
	}
	
	public void setModificationDate(String date) {
		modificationDateLabel.setText(date);
		modificationTextLabel.setVisible(true);
		modificationDateLabel.setVisible(true);
	}
	
    public void setMessage(String msg) {
      messageArea.setText(msg);
    }
}
