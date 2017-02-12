package de.htwsaar.wirth.server.service;

import static org.junit.Assert.*;

import org.junit.Test;

import de.htwsaar.wirth.remote.model.UserImpl;
import de.htwsaar.wirth.remote.model.interfaces.User;

/**
 * Created by olli on 08.02.17.
 */
public class UserServiceTest {
    UserServiceImpl service = new UserServiceImpl();

    @Test
    public void saveUserTest(){
        User user = new UserImpl("oseibert", "Oliver", "Seibert", "Passwort", true);

        int sizeBeforeSave = service.getAll().size();
        service.saveUser(user);
        int sizeAfterSave = service.getAll().size();

        assertEquals(sizeBeforeSave + 1, sizeAfterSave);
    }
}