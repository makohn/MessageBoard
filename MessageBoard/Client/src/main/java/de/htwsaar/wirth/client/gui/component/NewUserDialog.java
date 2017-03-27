package de.htwsaar.wirth.client.gui.component;


import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

/**
 * Class {@code NewUserDialog} opens a popup which allows to input a username and a password
 */
public class NewUserDialog extends Dialog<Pair<String, String>> {

    public NewUserDialog(){

        this.setTitle("Benutzer hinzufügen");
        ButtonType okButton = ButtonType.OK;
        this.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        Node loginButton = this.getDialogPane().lookupButton(okButton);
        loginButton.setDisable(true);

        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });
        this.getDialogPane().setContent(grid);
        this.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });
    }

}
