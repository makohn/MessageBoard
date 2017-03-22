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
	@FXML private Button editButton;
	@FXML private Button trashButton;
	
	private ClientImpl client = ClientImpl.getInstance();


	public void initEventHandler(MainViewController mainView, Message msg) {
		initEditEventHandler(mainView, msg);
		initDeleteEventHandler(mainView, msg);
	}
	
	private void initEditEventHandler(MainViewController mainView, Message msg) {
		editButton.setOnAction((actionEvent) -> {
			// TODO: read editedMsgTxt from User
			messageArea.setManaged(false);
			txtMessageEdit.setText(msg.getMessage());
			txtMessageEdit.setManaged(true);
			txtMessageEdit.requestFocus();
			txtMessageEdit.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
	            if (ke.getCode().equals(KeyCode.ENTER)) {
	            	editMessage(mainView, msg);
	            	ke.consume();
	            }
	        });
			txtMessageEdit.focusedProperty().addListener(new ChangeListener<Boolean>() {

				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					// Focus lost
					boolean focusLost = oldValue && !newValue;
					if(focusLost) {
						editMessage(mainView, msg);
					}
				}
				
			});			
		});
	}
	
	private void editMessage(MainViewController mainView, Message msg) {
		String editedMsgTxt = txtMessageEdit.getText();
		Task<Void> editTask = client.editMessage(editedMsgTxt, msg.getID());
		editTask.setOnFailed((workerStateEvent) -> {
			mainView.onError(workerStateEvent.getSource().getException());
		});
		mainView.getExec().submit(editTask);
		messageArea.setManaged(true);
		txtMessageEdit.setManaged(false);
	}

	private void initDeleteEventHandler(MainViewController mainView, Message msg) {
		trashButton.setOnAction((actionEvent) -> {
			Task<Void> deleteTask = client.deleteMessage(msg.getID());
			deleteTask.setOnFailed((workerStateEvent) -> {
				mainView.onError(workerStateEvent.getSource().getException());
			});
			mainView.getExec().submit(deleteTask);
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
