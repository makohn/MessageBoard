package de.htwsaar.wirth.remote.model;

import de.htwsaar.wirth.remote.model.interfaces.User;

import javax.persistence.*;
import java.rmi.server.UID;
import java.util.Date;

/**
 * Implements User-Interface
 * Created by stefanschloesser1 on 03.02.17.
 * Edited by oliverseibert on 08.02.17
 */
@Entity
@Table(name="users")
public class UserImpl implements User {
    @Column
    @Id
    private long id;
    @Column(unique = true)
    private String username;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String password;
    @Column
    private boolean isGroupLeader;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    /**
     * Constructor
     * @param username
     * @param firstName
     * @param lastName
     * @param password
     * @param isGroupLeader
     */
    public UserImpl(String username, String firstName, String lastName, String password, boolean isGroupLeader) {
        // this.id = new UID(); auto increment von der Datenbank beim ersten speichern eines Nutzers sinnvoll?
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.isGroupLeader = isGroupLeader;
        this.createdAt = new Date();
    }

    public long getID() {
        return id;
    }

    public String getUsername() { return username; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getPassword() { return password; }

    public boolean isGroupLeader() { return isGroupLeader; }

    public Date getCreatedAt() { return createdAt; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public void setPassword(String password) { this.password = password; }

    public void setGroupLeader(boolean groupLeader) { isGroupLeader = groupLeader; }

    @Override
    public String toString() {
        return "UserImpl{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", isGroupLeader=" + isGroupLeader +
                ", createdAt=" + createdAt +
                '}';
    }

}
