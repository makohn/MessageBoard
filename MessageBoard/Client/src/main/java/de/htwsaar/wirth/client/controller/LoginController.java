package de.htwsaar.wirth.client.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.htwsaar.wirth.client.ClientImpl;
import de.htwsaar.wirth.client.gui.ApplicationDelegate;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {

	@FXML
	private TextField txtUsername;
	@FXML
	private TextField txtPassword;
	@FXML
	private TextField txtHostname;
	@FXML
	private TextField txtPort;
	@FXML
	private TextField txtGroupName;
	@FXML
	private Button btnConnect;

	private ClientImpl client;
	
    private ExecutorService exec;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		client = ClientImpl.getInstance();
		exec = Executors.newCachedThreadPool();
	}

	public void initManager(final ApplicationDelegate delegate) {
		btnConnect.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				delegate.showLoadingHUD();
				login(delegate);
			}
		});
	}

	private void login(ApplicationDelegate delegate) {
		// TODO: NumberFormatException fangen
		
		Task<Void> task = client.login(txtUsername.getText(), txtPassword.getText(),txtHostname.getText(), Integer.parseInt(txtPort.getText()),txtGroupName.getText());
		task.setOnSucceeded(e -> {
			delegate.showMainScreen();
		});
		task.setOnFailed(e -> {
			onError(e.getSource().getException());
		});
		exec.submit(task);
	}
	
	private void onError(Throwable e) {
		// TODO: show fancy Error Message
        e.printStackTrace();
		ApplicationDelegate.getInstance().showLoginScreen();
	}
}
