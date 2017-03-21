package de.htwsaar.wirth.client.gui.component;

import java.text.DateFormat;

import de.htwsaar.wirth.client.controller.MainViewController;
import de.htwsaar.wirth.client.controller.MessageCellController;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

public class MessageCell extends ListCell<Message>{
	private Node graphic;
	private MessageCellController controller;
	private MainViewController mainView;
	
	 {
       try {
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MessageCell.fxml"));
           graphic = loader.load();
           controller = loader.getController();
       } catch (Exception exc) {
           throw new RuntimeException(exc);
       }
	 }

	 public MessageCell(MainViewController mainViewController) {
		 mainView = mainViewController;
	}

	@Override
	 protected void updateItem(Message msg, boolean empty) {
	        super.updateItem(msg, empty);
	        if(empty || msg == null) {
	        	setGraphic(null);
	        	setText(null);
	        } else {
	        	controller.setUsername(msg.getAuthor());
	        	controller.setGroup(msg.getGroup());
	        	controller.setDate(DateFormat.getDateTimeInstance().format(msg.getCreatedAt()));
	        	controller.setMessage(msg.getMessage());
	        	controller.initEventHandler(mainView, msg);
	            setGraphic(graphic);
	        }
	   }
}
