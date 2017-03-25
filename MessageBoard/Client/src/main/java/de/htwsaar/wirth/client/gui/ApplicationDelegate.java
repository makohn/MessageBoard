package de.htwsaar.wirth.client.gui;

import java.io.IOException;

import org.controlsfx.control.MaskerPane;

import de.htwsaar.wirth.client.controller.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

/**
 * Class {@code ApplicationDelegate} controls state transitions in the applications UI
 * It serves as the delegate of individual views, by which it can be accessed to load and
 * unload a requeted view.  
 * @author mkohn
 *
 */
public class ApplicationDelegate {

	private final static String LOGIN_VIEW_FXML_PATH = "/fxml/Login.fxml";
	private final static String MAIN_VIEW_FXML_PATH = "/fxml/Main.fxml";
	
	private static ApplicationDelegate instance;
	private Scene scene;
	private MaskerPane loadingHud;

	private ApplicationDelegate() {
		loadingHud = new MaskerPane();
		loadingHud.setText("Bitte warten");
	}

	//Singleton
	public static synchronized ApplicationDelegate getInstance() {
		if (instance == null) {
			instance = new ApplicationDelegate();
		}
		return instance;
	}

	/**
	 * Sets the underlying {@code Scene} for displaying the UI.
	 * @param scene - the scene in which UI containers should be embedded.
	 */
	public void setScene(Scene scene) {
		this.scene = scene;
	}

	/**
	 * Loads the LoginView and sets it as the root of the scene.
	 */
	public void showLoginScreen() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(LOGIN_VIEW_FXML_PATH));
			scene.setRoot((Parent) loader.load());
			LoginController controller = loader.<LoginController>getController();
			controller.initManager(ApplicationDelegate.getInstance());
		} catch (IOException ex) {
			// TODO
		}
	}

	/**
	 * Loads the MainView and sets it as the root of the scene.
	 */
	public void showMainScreen() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_VIEW_FXML_PATH));
			scene.setRoot((Parent) loader.load());
		} catch (IOException ex) {
			ex.printStackTrace();
			// TODO
		}
	}
	
	/**
	 * Shows a Loading HUD by adding it on top of a {@code StackPane} control.
	 */
	public void showLoadingHUD() {
		try {
			StackPane base = (StackPane) scene.getRoot();
			base.getChildren().add(loadingHud);
		} catch (ClassCastException ce) {
			//TODO
		}
	}
	
	/**
	 * Shows the LoginView again.
	 */
	public void logout() {
		showLoginScreen();
	}
}
