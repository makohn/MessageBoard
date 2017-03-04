package de.htwsaar.wirth.client.controller;

import java.net.URL;
import java.util.ResourceBundle;

import de.htwsaar.wirth.client.gui.ApplicationManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {
	
	@FXML private TextField txtUsername;
	@FXML private TextField txtPassword;
	@FXML private TextField txtHostname;
	@FXML private ComboBox<Integer> cmbPort;
	@FXML private Button btnConnect;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
	
	public void initManager(final ApplicationManager manager) {
		btnConnect.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				manager.login();
			}
		});
	}
}
