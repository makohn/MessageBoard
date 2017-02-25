package de.htwsaar.wirth.client.gui.component;

import de.htwsaar.wirth.client.controller.MessageCellController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

public class MessageCell extends ListCell<String>{
	private Node graphic;
	 private MessageCellController controller ;
	
	 {
       try {
           FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MessageCell.fxml"));
           graphic = loader.load();
           controller = loader.getController();
           controller.init();
       } catch (Exception exc) {
           throw new RuntimeException(exc);
       }
	 }

	 @Override
	 protected void updateItem(String msg, boolean empty) {
	        super.updateItem(msg, empty);
	        if(empty || msg == null) {
	          setGraphic(null);
	          setText(null);
	        } else {
	        	controller.setMessage(msg);
	            setGraphic(graphic);
	        }
	   }
}
