package de.htwsaar.wirth.server.dao;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import de.htwsaar.wirth.remote.model.interfaces.User;

/**
 * Database Access Object for model class {@code User}.
 * Created by stefanschloesser1 on 03.02.17.
 * Edited by Marius on 08.02.17
 * Edited by oliverseibert on 09.02.17
 */
public class UserDao extends AbstractDao<User>{
	
	private static final Logger logger = LogManager.getLogger(UserDao.class);
	/**
	 * Returns a list of all {@code User} Objects registered with
	 * the calling server instance.
	 * @return
	 */
    public List<User> getAll() {
        return query("from UserImpl");
    }

    /**
     * Returns a single user specified by its username
     * @param username - the unique username of the requested user
     * @return the requested {@code User} if existent
     */
    public User getUser(String username){
    	logger.debug("getUser::Requested user " + username);
        List<User> list = query("from UserImpl where username = '" + username +"'");
        if(list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    /**
     * Checks whether the calling server instance has already a
     * group leader assigned to it.
     * @return true, if there is a group leader,
     * 		   false, otherwise
     */
    public boolean existsGroupLeader(){
    	logger.debug("existsGroupLeader::");
        List<User> list = query("from UserImpl where isGroupLeader = true");
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).isGroupLeader()) return true;
        }
        return false;
    }
}
