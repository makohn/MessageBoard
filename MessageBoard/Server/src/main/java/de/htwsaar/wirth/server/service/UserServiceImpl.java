package de.htwsaar.wirth.server.service;

import de.htwsaar.wirth.remote.model.interfaces.User;
import de.htwsaar.wirth.server.dao.UserDao;
import de.htwsaar.wirth.server.service.interfaces.UserService;

import java.util.List;

/**
 * Created by olli on 08.02.17.
 */
public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDao();

    /**
     * Gibt alle User zurück
     * @return Liste mit User-Objekten
     */
    public List<User> getAll(){
    	synchronized (Services.class) {
    		return userDao.getAll();
    	}
    }

    /**
     * Speichert einen User
     * @param user User-Objekt
     */
    public void saveUser(User user){
    	synchronized (Services.class) {
    		userDao.save(user);
    	}
    }

    /**
     * Löscht einen User
     * @param user User-Objekt
     */
    public void deleteUser(User user){
    	synchronized (Services.class) {
    		userDao.delete(user);
    	}
    }

    /**
     * Sucht einen User anhand des eindeutigen Usernames
     * @param username Username als String
     * @return User-Objekt, wenn vorhanden ansonsten null
     */
    public User getUser(String username) { 
    	synchronized (Services.class) {
    		return userDao.getUser(username); 
    	}
    }

    /**
     * Gibt an, ob ein User bereits das Flag "isGroupLeader" beseitzt
     * @return true oder false
     */
    public boolean existsGroupLeader(){ 
    	synchronized (Services.class) {
    		return userDao.existsGroupLeader(); 
    	}
    }
}
