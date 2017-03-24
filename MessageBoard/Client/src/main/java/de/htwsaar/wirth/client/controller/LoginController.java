package de.htwsaar.wirth.client.controller;

import java.net.URL;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.server.ExportException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.htwsaar.wirth.client.ClientImpl;
import de.htwsaar.wirth.client.gui.ApplicationDelegate;
import de.htwsaar.wirth.client.util.PreferenceService;
import de.htwsaar.wirth.remote.exceptions.AuthenticationException;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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
        initTextFields();

    }

    private void initTextFields()
    {
        txtUsername.setText(PreferenceService.getInstance().getUsername());
        txtHostname.setText(PreferenceService.getInstance().getHostName());
        txtPort.setText(PreferenceService.getInstance().getPort());
        txtGroupName.setText(PreferenceService.getInstance().getGroupeName());
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
        int port;
		try {
		    port = Integer.parseInt(txtPort.getText());
		} catch(NumberFormatException e){
		    onError(e);
		    return;
        }
        PreferenceService.getInstance().setPreference(txtUsername.getText(),txtHostname.getText(),txtPort.getText(),txtGroupName.getText());
		Task<Void> task = client.login(	txtUsername.getText(), 
										txtPassword.getText(),
										txtHostname.getText(), 
										port,
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
        ApplicationDelegate.getInstance().showLoginScreen();
        try{
            throw e;
        } catch(NumberFormatException numberFormat){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Login");
            alert.setHeaderText("UPS! Es ist ein Fehler aufgetreten");
            alert.setContentText("Bitte geben Sie für den Port eine Zahl ein z.B. 40010");
            alert.showAndWait();

        } catch(AuthenticationException authEx){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Login");
            alert.setHeaderText("UPS! Es ist ein Fehler aufgetreten");
            alert.setContentText("Der Username oder das Passwort ist falsch");
            alert.showAndWait();

        } catch(NotBoundException groupNameException){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Login");
        alert.setHeaderText("UPS! Es ist ein Fehler aufgetreten");
        alert.setContentText("Der eingegebene Gruppenname existiert nicht");
        alert.showAndWait();

        }catch(ConnectException hostException){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Login");
            alert.setHeaderText("UPS! Es ist ein Fehler aufgetreten");
            alert.setContentText("Der Hostname wurde nicht gefunden. Der Hostname kann eine IP-Adresse sein oder ein Domainname, wird das Feld leer gelassen wird automatisch der Localhost angesprochen.");
            alert.showAndWait();

        }catch(ExportException portAlreadyInUse){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Login");
            alert.setHeaderText("UPS! Es ist ein Fehler aufgetreten");
            alert.setContentText("Der eingegebene Port wird bereits verwendet. Stellen Sie sicher, dass der eingebene Port nicht schon von einem anderen Programm benutzt wird");
            alert.showAndWait();

        }catch (Throwable ex){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("UPS! Es ist ein unbekannter Fehler aufgetreten");
            alert.setContentText("Bitte versuchen Sie es erneut");
            alert.showAndWait();
        }


    }
}
