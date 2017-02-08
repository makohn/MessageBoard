package de.htwsaar.wirth.server.service.interfaces;

import de.htwsaar.wirth.remote.model.interfaces.User;
import java.util.List;

/**
 * Created by olli on 08.02.17.
 */
public interface UserService {
    public List<User> getAll();
    public void saveUser(User user);
    public void deleteUser(User user);
}
