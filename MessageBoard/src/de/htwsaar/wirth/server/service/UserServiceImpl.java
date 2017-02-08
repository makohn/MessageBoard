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
        return userDao.getAll();
    }

    /**
     * Speichert einen User
     * @param user User-Objekt
     */
    public void saveUser(User user){
        userDao.save(user);
    }

    /**
     * Löscht einen User
     * @param user User-Objekt
     */
    public void deleteUser(User user){
        userDao.delete(user);
    }
}
