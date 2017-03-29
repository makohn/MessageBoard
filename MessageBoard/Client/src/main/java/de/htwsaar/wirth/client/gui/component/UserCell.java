package de.htwsaar.wirth.client.gui.component;

import de.htwsaar.wirth.client.controller.MainViewController;
import de.htwsaar.wirth.client.controller.UserCellController;
import de.htwsaar.wirth.remote.model.Status;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.util.Pair;


public class UserCell extends ListCell<Pair<String,Status>> {
	
	private MainViewController mainView;
	
	private Node graphic;
	private UserCellController controller;


	public UserCell(MainViewController mainView) {
		this.mainView = mainView;
		this.setBackground(null);
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UserCell.fxml"));
			graphic = loader.load();
			controller = loader.getController();
		} catch (Exception exc) {
			throw new RuntimeException(exc);
		}
	}

	@Override
	protected void updateItem(Pair<String, Status> item, boolean empty) {
	       super.updateItem(item, empty);
	       if(empty || item == null) {
	       	setGraphic(null);
	       	setText(null);
	       } else {
	       	 controller.initCell(mainView, item, this);
	         setGraphic(graphic);
	       }
	}
}
