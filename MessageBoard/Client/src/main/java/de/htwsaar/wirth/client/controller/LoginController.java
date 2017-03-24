package de.htwsaar.wirth.client.controller;

import de.htwsaar.wirth.client.ClientImpl;
import de.htwsaar.wirth.client.gui.ApplicationDelegate;
import de.htwsaar.wirth.client.util.LoginCacheService;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private LoginCacheService logServeice;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = ClientImpl.getInstance();
        exec = Executors.newCachedThreadPool();
        initTextFields();

    }

    private void initTextFields()
    {
        logServeice = new LoginCacheService();
        txtUsername.setText(logServeice.getUsername());
        txtHostname.setText(logServeice.getHostName());
        txtPort.setText(logServeice.getPort());
        txtGroupName.setText(logServeice.getGroupName());
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
        logServeice.saveData(txtUsername.getText(),txtHostname.getText(),txtPort.getText(),txtGroupName.getText());
        Task<Void> task = client.login(txtUsername.getText(), txtPassword.getText(), txtHostname.getText(), Integer.parseInt(txtPort.getText()), txtGroupName.getText());
        task.setOnSucceeded(e -> {
            delegate.showMainScreen();
        });
        task.setOnFailed(e -> {
            onError(e.getSource().getException());
        });
        exec.submit(task);
    }

<<<<<<< HEAD
	private void login(ApplicationDelegate delegate) {
		// TODO: NumberFormatException fangen
		
		Task<Void> task = client.login(	txtUsername.getText(), 
										txtPassword.getText(),
										txtHostname.getText(), 
										Integer.parseInt(txtPort.getText()),
										txtGroupName.getText());
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
=======
    private void onError(Throwable e) {
        // TODO: show fancy Error Message
>>>>>>> 3dc82204bb54099ba57561cbfa97ccfb1caeea59
        e.printStackTrace();
        ApplicationDelegate.getInstance().showLoginScreen();
    }
}
