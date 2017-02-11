package de.htwsaar.wirth.server.dao;

import java.util.List;

import de.htwsaar.wirth.remote.model.interfaces.User;

/**
 * Created by stefanschloesser1 on 03.02.17.
 * Edited by Marius on 08.02.17
 * Edited by oliverseibert on 09.02.17
 */
public class UserDao extends AbstractDao<User>{
    public List<User> getAll() {
        return query("from UserImpl");
    }

    public User getUser(String username){
        List<User> list = query("from UserImpl where username = " + username);
        if(list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    public boolean existsGroupLeader(){
        List<User> list = query("from UserImpl where isGroupLeader = true");
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).isGroupLeader()) return true;
        }
        return false;
    }
}
