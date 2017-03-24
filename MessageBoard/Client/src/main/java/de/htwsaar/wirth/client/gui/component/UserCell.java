package de.htwsaar.wirth.client.gui.component;

import de.htwsaar.wirth.client.ClientImpl;
import de.htwsaar.wirth.client.controller.MainViewController;
import de.htwsaar.wirth.remote.model.Status;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.Optional;

public class UserCell extends ListCell<Pair<String,Status>> {
	
	private HBox listEntry;
	private Text username;
	private StatusIndicator status;
	private MainViewController mainView;
	private Button delete = new Button();
    private Image image = new Image(getClass().getResourceAsStream("/img/Trash.png"));

    public UserCell (MainViewController mainView)
    {
        this.mainView = mainView;
    }
	@Override
	protected void updateItem(Pair<String, Status> item, boolean empty) {
		super.updateItem(item, empty);
        setBackground(null);

        if (!empty && item != null) {
    		listEntry = new HBox(5);
    		username = new Text("");
    		status = new StatusIndicator();
            if(ClientImpl.getInstance().isGroupLeader()){
                initDeletButton(item);
            }
            username.setText(item.getKey());
            username.setFill(Color.WHITE);
            
            status.setStatus(item.getValue());

            listEntry.getChildren().addAll(status, username,delete);
            listEntry.setAlignment(Pos.CENTER_LEFT);

            setGraphic(listEntry);
        }
    }
    private void initDeletButton(Pair<String, Status> item)
    {
            delete.setGraphic(new ImageView(image));
            delete.setVisible(false);
            delete.setManaged(false);
            this.setOnMouseEntered((e)->{
                delete.setVisible(true);
                delete.setManaged(true);
            });
            this.setOnMouseExited((e)->{
                delete.setVisible(false);
                delete.setManaged(false);
            });
            delete.setOnMouseClicked((e)-> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Benutzer Löschen");
                alert.setContentText("Wollen Sie diesen Benutzer dauerhaft löschen?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    Task<Void> deleteTask = ClientImpl.getInstance().deleteUser(item.getKey());
                    mainView.getExecutorService().submit(deleteTask);
                }

            });


    }
	
}
