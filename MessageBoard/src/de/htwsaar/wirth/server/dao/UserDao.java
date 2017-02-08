package de.htwsaar.wirth.server.dao;

import de.htwsaar.wirth.remote.model.interfaces.User;

import java.util.List;

/**
 * Created by stefanschloesser1 on 03.02.17.
 * Edited by Marius on 08.02.17
 */
public class UserDao extends AbstractDao<User>{
    public List<User> getAll() {
        return query("from Users");
    }

}
