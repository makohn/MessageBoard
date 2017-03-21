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
	@FXML private Button trashButton;
	private ClientImpl client = ClientImpl.getInstance();


	public void initEventHandler(Message msg){

		trashButton.setOnAction((e) -> {
			Task<Void> task = client.deleteMessage(msg.getID());
			new Thread(task).start();
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
