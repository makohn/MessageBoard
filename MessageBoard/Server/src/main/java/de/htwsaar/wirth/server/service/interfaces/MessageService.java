package de.htwsaar.wirth.server.service.interfaces;

import de.htwsaar.wirth.remote.model.interfaces.Message;

import java.util.List;

/**
 * Created by olli on 08.02.17.
 */
public interface MessageService {
    public List<Message> getAll();
    public List<Message> getUnpublished();
    public void saveMessage(Message message);
    public void deleteMessage(Message message);
}
