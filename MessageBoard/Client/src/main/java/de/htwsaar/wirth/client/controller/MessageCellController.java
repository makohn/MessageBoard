package de.htwsaar.wirth.client.controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MessageCellController {
	@FXML private Label messageArea;
	
    public void setMessage(String msg) {
      messageArea.setText(msg);
    }
}
