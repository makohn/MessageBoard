package de.htwsaar.wirth.server.service;

import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.server.dao.MessageDao;
import de.htwsaar.wirth.server.service.interfaces.MessageService;

import java.util.List;
import java.util.UUID;

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
	 * Gibt alle Messages einer Gruppe zurück
	 * @param group Name der Gruppe
	 * @return Liste mit Message-Objekten
	 */
	public List<Message> getAllMessagesByGroup(String group){
		synchronized (Services.class) {
			return messageDao.getAllMessagesByGroup(group);
		}
	}
    
    public Message getMessage(UUID id) {
    	synchronized (Services.class) {
    		return messageDao.getMessage(id);    		
    	}
    }

    /**
     * Speichert eine Message
     * @param message Message-Objekt
     */
    public void saveMessage(Message message){
    	synchronized (Services.class) {
    		Message oldMsg = Services.getInstance().getMessageService().getMessage(message.getID());
    		if ( (oldMsg == null) || (oldMsg.getModifiedAt().compareTo(message.getModifiedAt()) <= 0)) {
    			messageDao.save(message);
    		}
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

	@Override
	public void saveMessages(List<Message> messages) {
		for (Message m : messages)
			saveMessage(m);		
	}
}
