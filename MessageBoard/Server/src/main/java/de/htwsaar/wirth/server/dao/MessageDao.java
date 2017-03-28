package de.htwsaar.wirth.server.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.htwsaar.wirth.remote.model.interfaces.Message;

/**
 * Database Access Object for model class {@code Message}.
 * Created by stefanschloesser1 on 03.02.17.
 * Edited by oliverseibert on 23.03.17
 */
public class MessageDao extends AbstractDao<Message> {
	
	private static final Logger logger = LogManager.getLogger(MessageDao.class);
	
	/**
	 * Returns a list of all messages stored in the database of the
	 * calling server instance.
	 * @return - a list of {@code Message} objects
	 */
    public List<Message> getAll() {
    	logger.debug("getAll::");
        return query("from MessageImpl");
    }

	/**
	 * Returns a list of all messages filtered by a given group
	 * @param group - the specified groupname to filter the requested messages
	 * @return - a list of {@code Message} objects
	 */
    public List<Message> getAllMessagesByGroup(String group) {
    	logger.debug("getAllMessagesByGroup::Requested for group " + group);
        List<DatabaseQueryParameter> parameterList = new ArrayList<DatabaseQueryParameter>();
        parameterList.add(new DatabaseQueryParameter("group", group));
        List<Message> list = query("from MessageImpl where groupName = :group", parameterList);
        return list;
    }

    /**
     * Returns a limited list of all messages that were written/edited 
     * after a specified time.
     * @param zeit - the time restriction for the requested messages 
     * @param limit - a numeric limit for the requested messages
     * @return  @return - a list of {@code Message} objects
     */
    public List<Message> getMessagesAfterDate(Date zeit, int limit) {
    	logger.debug("getMessagesAfterDate::Date: " + zeit + " with limit: " + limit);
        List<DatabaseQueryParameter> parameterList = new ArrayList<DatabaseQueryParameter>();
        parameterList.add(new DatabaseQueryParameter("zeit", zeit));
        List<Message> list = query("from MessageImpl where createdAt > :zeit", parameterList, limit);
        return list;
    }

    /**
     * Returns a limited list of all messages that were written/edited 
     * after a specified time, filtered by a passed group name.
     * @param group - the specified groupname to filter the requested messages
     * @param zeit - the time restriction for the requested messages 
     * @param limit - a numeric limit for the requested messages
     * @return  @return - a list of {@code Message} objects
     */
    public List<Message> getMessagesByGroupAfterDate(String group, Date zeit, int limit) {
    	logger.debug("getMessagesByGroupAfterDate::Group: " + group + "; Date: " + zeit + " with limit: " + limit);
        List<DatabaseQueryParameter> parameterList = new ArrayList<DatabaseQueryParameter>();
        parameterList.add(new DatabaseQueryParameter("group", group));
        parameterList.add(new DatabaseQueryParameter("zeit", zeit));
        List<Message> list = query("from MessageImpl where groupName = :group and createdAt > :zeit", parameterList, limit);
        return list;
    }

    /**
     * Returns a single {@code Message} Object specified by its UUID.
     * @param id - the unique identifier of the requested {@code Message} Object
     * @return a {@code Message} Object if its id is found
     */
    public Message getMessage(UUID id) {
    	logger.debug("getMessage::with UUID: " + id);
    	List<DatabaseQueryParameter> parameterList = new ArrayList<DatabaseQueryParameter>();
        parameterList.add(new DatabaseQueryParameter("id", id));
        List<Message> list = query("from MessageImpl where id = :id", parameterList);
        if(list.size() > 0){
        	return list.get(0);
        }
        return null;
    }

    /**
     * Checks whether a passed Message exists in the database.
     * @param message - the message that should be checked
     * @return true, if the message exists
     * 		   false otherwise
     */
    public boolean existsMessage(Message message) {
    	logger.debug("existsMessage::Message UUID: " + message.getID());
        List<DatabaseQueryParameter> parameterList = new ArrayList<DatabaseQueryParameter>();
        parameterList.add(new DatabaseQueryParameter("id", message.getID()));
        List<Message> list = query("from MessageImpl where id = :id", parameterList);
        if(list.size() > 0){
            return true;
        }
        return false;
    }
}
