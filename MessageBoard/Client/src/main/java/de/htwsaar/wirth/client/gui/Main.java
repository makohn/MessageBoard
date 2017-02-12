package de.htwsaar.wirth.client.gui;

/**
 * Created by makohn on 12.02.17.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
       FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Main.fxml"));
       final Parent root = (Parent) loader.load();

        Scene scene = new Scene(root, 1040, 680);
        
        scene.getStylesheets().add("/css/main.css");

        stage.setTitle("MessageBoard");
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args) {
		launch(args);
	}
}
