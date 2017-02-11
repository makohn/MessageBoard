package de.htwsaar.wirth.remote.model.interfaces;

import java.util.Date;

/**
 * Created by stefanschloesser1 on 03.02.17.
 * Edited by oliverseibert on 09.02.17
 */
public interface User {
    public long getID();
    public String getUsername();
    public String getFirstName();
    public String getLastName();
    public String getPassword();
    public boolean isGroupLeader();
    public Date getCreatedAt();
    public void setFirstName(String firstName);
    public void setLastName(String lastName);
    public void setPassword(String password);
    public void setGroupLeader(boolean groupLeader);
}
