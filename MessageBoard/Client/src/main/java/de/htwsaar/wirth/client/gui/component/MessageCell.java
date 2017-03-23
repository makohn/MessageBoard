package de.htwsaar.wirth.client.gui.component;

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

	 public MessageCell(MainViewController mainViewController) {
		 mainView = mainViewController;
		 try {
			 FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MessageCell.fxml"));
	         graphic = loader.load();
	         controller = loader.getController();
	     } catch (Exception exc) {
	         throw new RuntimeException(exc);
	     }
	}

	@Override
	 protected void updateItem(Message msg, boolean empty) {
	        super.updateItem(msg, empty);
	        if(empty || msg == null) {
	        	setGraphic(null);
	        	setText(null);
	        } else {
	        	controller.initCell(mainView, msg);
	            setGraphic(graphic);
	        }
	   }
}
