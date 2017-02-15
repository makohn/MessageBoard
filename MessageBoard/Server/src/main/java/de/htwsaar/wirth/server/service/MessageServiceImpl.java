package de.htwsaar.wirth.server.service;

import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.dao.MessageDao;
import de.htwsaar.wirth.server.service.interfaces.MessageService;

import java.util.List;

/**
 * Created by olli on 08.02.17.
 * Edited by oliverseibert on 12.02.2017
 */
public class MessageServiceImpl implements MessageService {
    private MessageDao messageDao = new MessageDao();

    /**
     * Gibt alle Messages zurück
     * @return Liste mit Message-Objekten
     */
    public List<Message> getAll(){
    	synchronized (Services.class) {
    		return messageDao.getAll();
    	}
    }

    /**
     * Überprüft, ob es die übergebene Nachricht bereits in der Datenhaltungsschicht existiert
     * @return true / false
     */
    public boolean existsMessage(Message message) { 
    	synchronized (Services.class) {
    		return messageDao.existsMessage(message); 
    	}
    }

    /**
     * Speichert eine Message
     * @param message Message-Objekt
     */
    public void saveMessage(Message message){
    	synchronized (Services.class) {
    		messageDao.save(message);
    	}
    }

    /**
     * Löscht eine Message
     * @param message MessageDao-Objekt
     */
    public void deleteMessage(Message message){
    	synchronized (Services.class) {
    		messageDao.delete(message);
    	}
    }
}
