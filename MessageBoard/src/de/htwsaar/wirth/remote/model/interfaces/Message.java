package de.htwsaar.wirth.remote.model.interfaces;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * Created by stefanschloesser1 on 03.02.17.
 * Edited by oliverseibert on 09.02.17
 */
public interface Message
{
    public UUID getID();
    public String getGroup();
    public boolean isPublic();
    public boolean isPublished();
    public String getMessage();
    public String getAuthor();
    public Date getCreatedAt();
    public Date getModifiedAt();
    public void setPublished(boolean published);
    public void changeMessage(String msg);
}
