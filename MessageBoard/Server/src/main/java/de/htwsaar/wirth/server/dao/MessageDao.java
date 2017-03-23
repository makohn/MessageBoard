package de.htwsaar.wirth.server.dao;

import de.htwsaar.wirth.remote.model.interfaces.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by stefanschloesser1 on 03.02.17.
 * Edited by oliverseibert on 23.03.17
 */
public class MessageDao extends AbstractDao<Message> {

    public List<Message> getAll() {
        return query("from MessageImpl");
    }

    public List<Message> getAllMessagesByGroup(String group) {
        List<DatabaseQueryParameter> parameterList = new ArrayList<DatabaseQueryParameter>();
        parameterList.add(new DatabaseQueryParameter("group", group));
        List<Message> list = query("from MessageImpl where groupName = :group", parameterList);
        return list;
    }

    public List<Message> getAllMessagesAfterDate(Date zeit, int limit) {
        List<DatabaseQueryParameter> parameterList = new ArrayList<DatabaseQueryParameter>();
        parameterList.add(new DatabaseQueryParameter("zeit", zeit));
        List<Message> list = query("from MessageImpl where createdAt > :zeit", parameterList, limit);
        return list;
    }
    
    public Message getMessage(UUID id) {
    	List<DatabaseQueryParameter> parameterList = new ArrayList<DatabaseQueryParameter>();
        parameterList.add(new DatabaseQueryParameter("id", id));
        List<Message> list = query("from MessageImpl where id = :id", parameterList);
        if(list.size() > 0){
        	return list.get(0);
        }
        return null;
    }

    public boolean existsMessage(Message message) {
        List<DatabaseQueryParameter> parameterList = new ArrayList<DatabaseQueryParameter>();
        parameterList.add(new DatabaseQueryParameter("id", message.getID()));
        List<Message> list = query("from MessageImpl where id = :id", parameterList);
        if(list.size() > 0){
            return true;
        }
        return false;
    }
}
