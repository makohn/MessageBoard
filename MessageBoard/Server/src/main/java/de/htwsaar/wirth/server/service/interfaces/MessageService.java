package de.htwsaar.wirth.server.service.interfaces;

import de.htwsaar.wirth.remote.model.interfaces.Message;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by olli on 08.02.17.
 * Edited by oliverseibert on 12.02.17
 */
public interface MessageService {
    public List<Message> getAll();
    public List<Message> getAllMessagesByGroup(String group);
    public List<Message> getMessagesAfterDate(Date zeit, int limit);
    public List<Message> getMessagesByGroupAfterDate(String group, Date zeit, int limit);
    public Message getMessage(UUID id);
    public void saveMessage(Message message);
    public void saveMessages(List<Message> messages);
    public void deleteMessage(Message message);
}
