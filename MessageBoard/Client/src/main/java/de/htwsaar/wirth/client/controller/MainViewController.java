package de.htwsaar.wirth.client.controller;

import de.htwsaar.wirth.client.ClientImpl;
import de.htwsaar.wirth.client.gui.ApplicationDelegate;
import de.htwsaar.wirth.client.gui.component.MessageCell;
import de.htwsaar.wirth.client.gui.component.NewUserDialog;
import de.htwsaar.wirth.client.gui.component.UserCell;
import de.htwsaar.wirth.client.util.ExceptionUtil;
import de.htwsaar.wirth.client.util.UIConstants;
import de.htwsaar.wirth.remote.exceptions.*;
import de.htwsaar.wirth.remote.model.Status;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import org.controlsfx.control.textfield.CustomTextField;

import java.net.URL;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;

public class MainViewController implements Initializable {

    @FXML
    private Button groupButton;
    @FXML
    private CustomTextField txtSearch;
    @FXML
    private ListView<Message> chatPane;
    @FXML
    private ListView<String> groupList;
    @FXML
    private ListView<Pair<String, Status>> userList;
    @FXML
    private TextArea messageBox;
    @FXML
    private Label reloadLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label fullNameLabel;
    @FXML
    private ComboBox<Status> cmbStatus;
    @FXML
    private Label lblOwnStatus;
    @FXML
    private ToggleButton toggleUserList;
    @FXML
    private ToggleButton toggleGroupList;
    @FXML
    private VBox userArea;
    @FXML
    private VBox groupArea;
    @FXML
    private Button btnAllFilter;
    @FXML
    private Button btnAddUser;
    @FXML
    private Button btnRefresh;
    @FXML
    private Button btnLogout;

    private ObservableList<Message> messages;
    private FilteredList<Message> filteredAndSortedList;
    private ObservableList<String> groups;
    private ObservableList<Pair<String, Status>> users;

    private Predicate<Message> groupFilter;

    private ClientImpl client;

    private ExecutorService exec;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        exec = Executors.newCachedThreadPool();

        client = ClientImpl.getInstance();
        client.setView(this);

        groupButton.setText(client.getGroupName());
        usernameLabel.setText(client.getUsername());
        fullNameLabel.setText(client.getUsername());

        // Messages
        messages = FXCollections.observableArrayList();
        ObservableList<Message> sortedWrapperList = messages.sorted((m1, m2) -> {
            return m1.getCreatedAt().compareTo(m2.getCreatedAt());
        });
        filteredAndSortedList = new FilteredList<Message>(sortedWrapperList);
        chatPane.setSelectionModel(new NoSelectionModel());
        chatPane.setCellFactory(list -> new MessageCell(this));
        chatPane.setItems(filteredAndSortedList);

        // Users
        users = FXCollections.observableArrayList();
        ObservableList<Pair<String, Status>> sortedUserList = users.sorted((p1, p2) -> {
            return p1.getKey().compareTo(p2.getKey());
        });
        userList.setCellFactory(list -> new UserCell(this));
        userList.setItems(sortedUserList);

        // Groups
        groups = FXCollections.observableArrayList();
        ObservableList<String> sortedGroupList = groups.sorted((g1, g2) -> {
            return g1.compareTo(g2);
        });
        groupList.setItems(sortedGroupList);

        // Status
        cmbStatus.setItems(FXCollections.observableArrayList(Status.values()));
        lblOwnStatus.setText(UIConstants.STATUS_SYMBOL_FILLED);
        cmbStatus.valueProperty().addListener(new ChangeListener<Status>() {
            @Override
            public void changed(ObservableValue<? extends Status> observable, Status oldValue, Status newValue) {
                lblOwnStatus.setTextFill(newValue.getColor());
                Task<Void> changeStatusTask = client.changeUserStatus(newValue);
                changeStatusTask.setOnFailed(e -> {
                    onError(e.getSource().getException());
                });
                exec.submit(changeStatusTask);
            }
        });

        cmbStatus.getSelectionModel().select(Status.ONLINE);
        refreshAllMessages(true);
        refreshAllUserStatus();
        initSendMessageButton();
        initAllFilterButton();
        initGroupFilter();
        initRefreshButton();
        initLogoutButton();
        initSearchField();
        initAddUserButton();
    }

    /**
     * Sets a action event on the Search-Field and binds a Search-Filter to the search-field
     */
    private void initSearchField() {
        txtSearch.setOnAction((actionEvent) -> {
            // wenn das SearchField nicht leer ist
            if (txtSearch.getText().trim().isEmpty()) {
                filteredAndSortedList.setPredicate(groupFilter);
            } else {
                // erstelle einen searchFilter
                Predicate<Message> searchFilter = msg -> msg.getMessage().contains(txtSearch.getText()) || msg.getAuthor().contains(txtSearch.getText());
                if (groupFilter != null) {
                    // wenn ein GruppenFilter existiert verunde beide Filter
                    Predicate<Message> searchAndGroupFilter = msg -> searchFilter.test(msg) && groupFilter.test(msg);
                    filteredAndSortedList.setPredicate(searchAndGroupFilter);
                } else {
                    // ansonsten aktiviere nur den SearchFilter
                    filteredAndSortedList.setPredicate(searchFilter);
                }
            }
        });
    }

    /**
     * Sets a Returnkey-event on the Message-textfield to send the message to the server.
     */
    private void initSendMessageButton() {
        /* Added to prevent the enter from adding a new line to inputMessageBox */
        messageBox.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
        	boolean isEnterPressed = ke.getCode().equals(KeyCode.ENTER);
        	if (ke.isShiftDown() && isEnterPressed) {
        		messageBox.appendText("\n");
        		ke.consume();
        	} else if (isEnterPressed) {
                sendMessage();
                ke.consume();
            }
        });
    }

    /**
     * Sets a action event on the Showall-Button to clear the filter.
     */
    private void initAllFilterButton() {
        btnAllFilter.setOnAction(e -> {
            // setPredicate(null), entfernt jegliche Filterung
            groupFilter = null;
            filteredAndSortedList.setPredicate(null);
            // entferne den Suchtext aus dem Suchfeld, da nun eine Gruppenfilterung ausgewählt wurde
            txtSearch.clear();
        });
    }

    /**
     * Sets a action event on the Group-Buttons to filter the list of messages by a specified group.
     */
    private void initGroupFilter() {
        groupList.setOnMouseClicked((mouseEvent) -> {
            String clickedGroup = groupList.getSelectionModel().getSelectedItem();
            groupFilter = msg -> clickedGroup.equals(msg.getGroup());
            filteredAndSortedList.setPredicate(groupFilter);
            // entferne den Suchtext aus dem Suchfeld, da nun eine Gruppenfilterung ausgewählt wurde
            txtSearch.clear();
        });
    }

    /**
     *  Sets a action event on the Refresh-Button to force the serve to send all messages and user statuses
     */
    private void initRefreshButton() {
        btnRefresh.setOnAction((actionEv) -> {
            refreshAllMessages(false);
            refreshAllUserStatus();
        });
    }

    /**
     * Sets a action event on the Adduser-Button which opens a dialog to crreate a new user.
     */
    private void initAddUserButton() {
        btnAddUser.setOnAction((actionEv) -> {
            Dialog<Pair<String, String>> dialog = new NewUserDialog();
            Optional<Pair<String, String>> result = dialog.showAndWait();
            result.ifPresent(usernamePassword -> {
                addUser(usernamePassword.getKey(), usernamePassword.getValue());
                refreshAllUserStatus();
            });
        });
    }

    /**
     * Sets a action event on the Logout-Button which log out the user and delegate to the Login-Screen
     */
    private void initLogoutButton() {
        btnLogout.setOnAction((actionEv) -> {
            ApplicationDelegate.getInstance().showLoadingHUD();
            Task<Void> logoutTask = client.logout();
            logoutTask.setOnSucceeded((e) -> {
                ApplicationDelegate.getInstance().logout();
            });
            logoutTask.setOnFailed((e) -> {
                onError(e.getSource().getException());
            });
            exec.submit(logoutTask);
        });
    }

    /**
     * Forces the server to send all messages. All messages in the UI will be replaced by the incoming messages.
     * @param shouldScrollToLast
     */
    private void refreshAllMessages(boolean shouldScrollToLast) {
        Task<List<Message>> messageTask = client.getAllMessages();
        messageTask.setOnSucceeded((e) -> {
            List<Message> messageList = messageTask.getValue();
            messages.clear();
            for (Message msg : messageList) {
                insertMessage(msg);
            }
            if (shouldScrollToLast)
                scrollToLastMessage();
        });
        messageTask.setOnFailed((e) -> {
            onError(e.getSource().getException());
        });
        exec.submit(messageTask);
    }

    /**
     * Forces the server to send all user statuses. All user statuses in the UI will be replaced by the incoming statuses.
     * @param shouldScrollToLast
     */
    private void refreshAllUserStatus() {
        Task<Map<String, Status>> getUserStatusTask = client.getUserStatus();
        getUserStatusTask.setOnSucceeded((e) -> {
            Map<String, Status> userStatusMap = getUserStatusTask.getValue();
            users.clear();
            for (Entry<String, Status> entry : userStatusMap.entrySet()) {
                users.add(new Pair<String, Status>(entry.getKey(), entry.getValue()));
            }
        });
        getUserStatusTask.setOnFailed((e) -> {
            onError(e.getSource().getException());
        });
        exec.submit(getUserStatusTask);
    }

    /**
     * Reload the UI-Userlist.
     */
    private void refreshUserList() {
        ObservableList<Pair<String, Status>> sortedUserList = users.sorted(Comparator.comparing(Pair::getKey));
        userList.setCellFactory(list -> new UserCell(this));
        userList.setItems(sortedUserList);
    }

    /**
     * Scroll to the last message in the list
     */
    private void scrollToLastMessage() {
        if (!chatPane.getItems().isEmpty())
            chatPane.scrollTo(chatPane.getItems().size() - 1);
    }

    /**
     * Insert a Message in the messagelist
     * @param msg
     */
    public void insertMessage(Message msg) {
        messages.remove(msg);
        messages.add(msg);

        if (!groups.contains(msg.getGroup())) {
            groups.add(msg.getGroup());
        }
    }

    /**
     * Update a Message in the messagelist
     * @param msg
     */
    public void editMessage(Message msg) {
        messages.remove(msg);
        messages.add(msg);
    }


    public void deleteMessage(Message msg) {
        messages.remove(msg);
    }

    public void changeUserStatus(String username, Status status) {
        users.removeIf((pair) -> username.equals(pair.getKey()));
        users.add(new Pair<String, Status>(username, status));
    }

    public void deleteUser(String username) {
        users.removeIf((pair) -> username.equals(pair.getKey()));
        refreshUserList();
    }

    public void addUser(String username, String password) {
        Task<Void> addUserTask = client.addUser(username, password);
        addUserTask.setOnFailed((e) -> onError(e.getSource().getException()));
        exec.submit(addUserTask);
    }

    public void sendMessage() {
        if (!messageBox.getText().isEmpty()) {
            Task<Void> sendMessageTask = client.sendMessage(messageBox.getText());
            sendMessageTask.setOnFailed((e) -> {
                onError(e.getSource().getException());
            });
            exec.submit(sendMessageTask);
            messageBox.clear();
        }
    }

    /**
     * Specifies the thrown exceptions from the MainViewController and
     * creates an individual Alert-message for each
     * @param e
     */
    public void onError(Throwable e) {
        try {
            throw e;
        } catch (UserAlreadyExistsException userAlreadyExist) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(ExceptionUtil.USER_ALREADY_EXISTS.getLocation());
            alert.setHeaderText(ExceptionUtil.USER_ALREADY_EXISTS.getDefaultText());
            alert.setContentText(ExceptionUtil.USER_ALREADY_EXISTS.toString());
            alert.showAndWait();
        } catch (NoPermissionException noPermission) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(ExceptionUtil.NO_PERMISSION.getLocation());
            alert.setHeaderText(ExceptionUtil.NO_PERMISSION.getDefaultText());
            alert.setContentText(ExceptionUtil.NO_PERMISSION.toString());
            alert.showAndWait();
        } catch (UserNotExistsException noUserExist) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(ExceptionUtil.UNKNOWN_USER.getLocation());
            alert.setHeaderText(ExceptionUtil.UNKNOWN_USER.getDefaultText());
            alert.setContentText(ExceptionUtil.UNKNOWN_USER.toString());
            alert.showAndWait();
        } catch (MessageNotExistsException messageNotExist) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(ExceptionUtil.MESSAGE_NOT_EXISTS.getLocation());
            alert.setHeaderText(ExceptionUtil.MESSAGE_NOT_EXISTS.getDefaultText());
            alert.setContentText(ExceptionUtil.MESSAGE_NOT_EXISTS.toString());
            alert.showAndWait();
        } catch (AuthenticationException |  NotLoggedInException session) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(ExceptionUtil.AUTHENTIFICATION_ERROR.getLocation());
            alert.setHeaderText(ExceptionUtil.AUTHENTIFICATION_ERROR.getDefaultText());
            alert.setContentText(ExceptionUtil.AUTHENTIFICATION_ERROR.toString());
            alert.showAndWait();
            Task<Void> logoutTask = ClientImpl.getInstance().logout();
            exec.submit(logoutTask);
            ApplicationDelegate.getInstance().showLoginScreen();
        } catch (Throwable unknown) {
            unknown.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(ExceptionUtil.UNKNOWN_ERROR_MESSAGEBOARD.getLocation());
            alert.setHeaderText(ExceptionUtil.UNKNOWN_ERROR_MESSAGEBOARD.getDefaultText());
            alert.setContentText(ExceptionUtil.UNKNOWN_ERROR_MESSAGEBOARD.toString());
            alert.showAndWait();
            Task<Void> logoutTask = ClientImpl.getInstance().logout();
            exec.submit(logoutTask);
            ApplicationDelegate.getInstance().showLoginScreen();
        }
    }

    public ExecutorService getExecutorService() {
        return exec;
    }

}

