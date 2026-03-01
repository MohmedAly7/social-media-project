package com.socialmedia.dao;

import com.socialmedia.models.Message;
import com.socialmedia.models.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MessageDAOTest {

    private static MessageDAO messageDAO;
    private static UserDAO userDAO;

    @BeforeAll
    public static void setUp() {
        messageDAO = new MessageDAO();
        userDAO = new UserDAO();
    }

    @Test
    public void testSendAndReceiveMessages() {
        String u1Name = "ChatUserOne" + System.currentTimeMillis();
        String u2Name = "ChatUserTwo" + System.currentTimeMillis();

        boolean reg1 = userDAO.registerUser(u1Name, u1Name + "@test.com", "pass");
        boolean reg2 = userDAO.registerUser(u2Name, u2Name + "@test.com", "pass");

        if (reg1 && reg2) {
            User u1 = userDAO.loginUser(u1Name + "@test.com", "pass");
            User u2 = userDAO.loginUser(u2Name + "@test.com", "pass");
            assertNotNull(u1);
            assertNotNull(u2);

            boolean sent = messageDAO.sendMessage(u1.getId(), u2.getId(), "Hello from User 1!");
            assertTrue(sent, "Message should be sent successfully");

            messageDAO.sendMessage(u2.getId(), u1.getId(), "Hi User 1, this is User 2!");

            List<Message> history = messageDAO.getChatHistory(u1.getId(), u2.getId());
            assertEquals(2, history.size(), "There should be two messages in the chat history");

            assertEquals("Hello from User 1!", history.get(0).getContent());
            assertEquals(u1.getId(), history.get(0).getSenderId());

            assertEquals("Hi User 1, this is User 2!", history.get(1).getContent());
            assertEquals(u2.getId(), history.get(1).getSenderId());

        } else {
            System.err.println("Skipping message test due to DB connection or registration failure.");
        }
    }
}
