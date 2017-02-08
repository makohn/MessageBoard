package de.htwsaar.wirth.remote.model.interfaces;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by stefanschloesser1 on 03.02.17.
 */
public interface Message
{
    public String getMessage();

    public void changeMessage(String msg);

    public LocalDateTime getTime();

    public UUID getID();

    public String getAuthor();

    public String getSection();

    public void setSection(String section);
}
