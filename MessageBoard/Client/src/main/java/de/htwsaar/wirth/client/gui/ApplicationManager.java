package de.htwsaar.wirth.client.gui;

import java.io.IOException;

import org.controlsfx.control.MaskerPane;

import de.htwsaar.wirth.client.controller.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;


public class ApplicationManager {

	private static ApplicationManager instance;
	private Scene scene;
	private MaskerPane loadingHud;

	private ApplicationManager() {
		loadingHud = new MaskerPane();
		loadingHud.setText("Bitte warten");
	}

	public static synchronized ApplicationManager getInstance() {
		if (instance == null) {
			instance = new ApplicationManager();
		}
		return instance;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public void showLoginScreen() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
			scene.setRoot((Parent) loader.load());
			LoginController controller = loader.<LoginController>getController();
			controller.initManager(ApplicationManager.getInstance());
		} catch (IOException ex) {
			// TODO
		}
	}

	public void showMainScreen(/* String sessionID */) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
			scene.setRoot((Parent) loader.load());
		} catch (IOException ex) {
			// TODO
		}
	}
	
	public void showLoadingHUD() {
		try {
			StackPane base = (StackPane) scene.getRoot();
			base.getChildren().add(loadingHud);
		} catch (ClassCastException ce) {
			//TODO
		}
	}
	
	public void logout() {
		showLoginScreen();
	}
	
	public void login() {
		showMainScreen();
	}
}
