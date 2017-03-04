package de.htwsaar.wirth.client.gui;

/**
 * Created by makohn on 12.02.17.
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static final int DEFAULT_STAGE_WIDTH  = 900;
	private static final int DEFAULT_STAGE_HEIGHT = 700;
	
	private ApplicationManager manager;

	@Override
	public void start(Stage stage) throws Exception {
		Image icon = new Image(this.getClass().getResourceAsStream("/img/icon-1024-mac.png"));
		stage.getIcons().add(icon);
		Scene scene = new Scene(new StackPane());
		
		manager = ApplicationManager.getInstance();
		manager.setScene(scene);
		manager.showLoginScreen();
		
		stage.setHeight(DEFAULT_STAGE_HEIGHT);
		stage.setWidth(DEFAULT_STAGE_WIDTH);
		stage.setScene(scene);
		
		stage.show();
    }
    
    public static void main(String[] args) {
		launch(args);
	}
}
