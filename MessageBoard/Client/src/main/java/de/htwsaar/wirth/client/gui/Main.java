package de.htwsaar.wirth.client.gui;

/**
 * Main entry point to the UI.
 * @author mkohn
 *
 */
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static final int DEFAULT_STAGE_WIDTH  = 1000;
	private static final int DEFAULT_STAGE_HEIGHT = 700;
	
	private ApplicationDelegate delegate;
	
	@Override
	public void start(Stage stage) throws Exception {
		Image icon = new Image(this.getClass().getResourceAsStream("/img/icon-1024-mac.png"));
		stage.getIcons().add(icon);
		Scene scene = new Scene(new StackPane());
		
		delegate = ApplicationDelegate.getInstance();
		delegate.setScene(scene);
		delegate.showLoginScreen();
		
		stage.setHeight(DEFAULT_STAGE_HEIGHT);
		stage.setWidth(DEFAULT_STAGE_WIDTH);
		stage.setScene(scene);
		
		stage.show();
    }
    
	@Override
	public void stop() throws Exception {
		Platform.exit();
		System.exit(0);
	}
	
    public static void main(String[] args) {
		launch(args);
	}
}
