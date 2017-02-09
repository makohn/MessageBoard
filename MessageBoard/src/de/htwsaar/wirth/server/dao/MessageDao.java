package de.htwsaar.wirth.server.dao;

import de.htwsaar.wirth.remote.model.interfaces.Message;

import java.util.List;

/**
 * Created by stefanschloesser1 on 03.02.17.
 * Edited by Marius on 08.02.17
 */
public class MessageDao extends AbstractDao<Message> {

    public List<Message> getAll() {
        return query("from MessageImpl");
    }

}
