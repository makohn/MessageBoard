package de.htwsaar.wirth.client.controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.htwsaar.wirth.client.ClientImpl;
import de.htwsaar.wirth.client.gui.ApplicationDelegate;
import de.htwsaar.wirth.remote.model.auth.LoginPacket;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {

	@FXML
	private TextField txtUsername;
	@FXML
	private TextField txtPassword;
	@FXML
	private TextField txtHostname;
	@FXML
	private ComboBox<Integer> cmbPort;
	@FXML
	private Button btnConnect;

	private ClientImpl client;
	
    private ExecutorService exec = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r);
        t.setDaemon(true); 
        return t ;
    });
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Task<Void> task = new Task<Void>() {
		    @Override public Void call() {
				client = ClientImpl.getInstance();
		        return null;
		    }
		};
		exec.submit(task);
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
		Task<Void> task = new Task<Void>() {
		    @Override public Void call() {
				LoginPacket login = new LoginPacket(txtUsername.getText(), txtPassword.getText());
				client.connect(login);
		        return null;
		    }
		};

		task.setOnSucceeded((e) -> {
			delegate.showMainScreen();
		});
		task.setOnFailed((e) -> {
		  //TODO
		});
		exec.submit(task);
	}
}
