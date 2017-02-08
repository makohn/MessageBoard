package de.htwsaar.wirth.server.service;

import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.dao.MessageDao;
import de.htwsaar.wirth.server.service.interfaces.MessageService;

import java.util.List;

/**
 * Created by olli on 08.02.17.
 */
public class MessageServiceImpl implements MessageService {
    private MessageDao messageDao = new MessageDao();

    /**
     * Gibt alle Messages zurück
     * @return Liste mit Message-Objekten
     */
    public List<Message> getAll(){
        return messageDao.getAll();
    }

    /**
     * Speichert eine Message
     * @param message Message-Objekt
     */
    public void saveMessage(Message message){
        messageDao.save(message);
    }

    /**
     * Löscht eine Message
     * @param message MessageDao-Objekt
     */
    public void deleteMessage(Message message){
        messageDao.delete(message);
    }
}
