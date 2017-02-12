package de.htwsaar.wirth.client.controller;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class MessageCellController {
	@FXML private Text messageArea;
	
    public void setMessage(String msg) {
      messageArea.setText(msg);
      messageArea.setWrappingWidth(500);
    }
}
