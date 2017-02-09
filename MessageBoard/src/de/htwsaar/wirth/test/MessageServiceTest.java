package de.htwsaar.wirth.test;

import de.htwsaar.wirth.server.service.MessageServiceImpl;
import de.htwsaar.wirth.remote.model.interfaces.Message;
import de.htwsaar.wirth.remote.model.MessageImpl;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by olli on 08.02.17.
 */
@RunWith(JUnitPlatform.class)
public class MessageServiceTest {
    MessageServiceImpl service = new MessageServiceImpl();

    @Test
    void saveMessageTest(){
        Message message = new MessageImpl("Testnachricht", "oseibert", "Wirth", false);

        int sizeBeforeSave = service.getAll().size();
        service.saveMessage(message);
        int sizeAfterSave = service.getAll().size();

        assertEquals(sizeBeforeSave + 1, sizeAfterSave);
    }
}
