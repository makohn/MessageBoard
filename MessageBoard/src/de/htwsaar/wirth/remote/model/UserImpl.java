package de.htwsaar.wirth.remote.model;

import de.htwsaar.wirth.remote.model.interfaces.User;
import java.time.LocalDateTime;

/**
 * Implements User-Interface
 * Created by stefanschloesser1 on 03.02.17.
 * Edited by oliverseibert on 07.02.17
 */
public class UserImpl implements User{
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private boolean isGroupLeader;
    private LocalDateTime createdAt;

    /**
     * Constructor
     * @param username
     * @param firstName
     * @param lastName
     * @param password
     * @param isGroupLeader
     * @param createdAt
     */
    public UserImpl(String username, String firstName, String lastName, String password, boolean isGroupLeader, LocalDateTime createdAt) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.isGroupLeader = isGroupLeader;
        this.createdAt = createdAt;
    }

    public String getUsername() { return username; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getPassword() { return password; }

    public boolean isGroupLeader() { return isGroupLeader; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public void setPassword(String password) { this.password = password; }

    public void setGroupLeader(boolean groupLeader) { isGroupLeader = groupLeader; }

    @Override
    public String toString() {
        return "UserImpl{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", isGroupLeader=" + isGroupLeader +
                ", createdAt=" + createdAt +
                '}';
    }
}
