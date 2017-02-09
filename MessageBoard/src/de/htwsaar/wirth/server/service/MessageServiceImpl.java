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
     * Gibt alle Messages zurück, die öffentlich sind, aber noch nicht veröffentlicht wurden
     * @return Liste mit noch nicht veröffentlichen Message-Objekten
     */
    public List<Message> getUnpublished() { return messageDao.getUnpublished(); }

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
