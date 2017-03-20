package de.htwsaar.wirth.client.controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MessageCellController {
	@FXML private Label usernameLabel;
	@FXML private Label groupLabel;
	@FXML private Label dateLabel;
	@FXML private Label messageArea;
	
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
