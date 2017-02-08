package de.htwsaar.wirth.test;

import de.htwsaar.wirth.server.service.UserServiceImpl;
import de.htwsaar.wirth.remote.model.interfaces.User;
import de.htwsaar.wirth.remote.model.UserImpl;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by olli on 08.02.17.
 */
@RunWith(JUnitPlatform.class)
public class UserServiceTest {
    UserServiceImpl service = new UserServiceImpl();

    @Test
    void saveUserTest(){
        User user = new UserImpl("oseibert", "Oliver", "Seibert", "Passwort", true);

        int sizeBeforeSave = service.getAll().size();
        service.saveUser(user);
        int sizeAfterSave = service.getAll().size();

        assertEquals(sizeBeforeSave + 1, sizeAfterSave);
    }
}
