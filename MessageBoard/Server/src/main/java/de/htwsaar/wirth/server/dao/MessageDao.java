package de.htwsaar.wirth.server.dao;

import de.htwsaar.wirth.remote.model.interfaces.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stefanschloesser1 on 03.02.17.
 * Edited by Marius on 08.02.17
 * Edited by oliverseibert on 12.02.17
 */
public class MessageDao extends AbstractDao<Message> {

    public List<Message> getAll() {
        return query("from MessageImpl");
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
