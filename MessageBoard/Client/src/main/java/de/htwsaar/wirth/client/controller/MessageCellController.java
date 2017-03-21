package de.htwsaar.wirth.client.controller;
import de.htwsaar.wirth.client.ClientImpl;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MessageCellController {
	@FXML private Label usernameLabel;
	@FXML private Label groupLabel;
	@FXML private Label dateLabel;
	@FXML private Label messageArea;
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
			String editedMsgTxt = "abc";
			Task<Void> editTask = client.editMessage(editedMsgTxt, msg.getID());
			editTask.setOnFailed((workerStateEvent) -> {
				mainView.onError(workerStateEvent.getSource().getException());
			});
			mainView.getExec().submit(editTask);
		});
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
	
	public void setDate(String date) {
		dateLabel.setText(date);
	}
	
    public void setMessage(String msg) {
      messageArea.setText(msg);
    }
}
