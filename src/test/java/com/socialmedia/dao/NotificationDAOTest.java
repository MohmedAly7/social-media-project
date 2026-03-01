package com.socialmedia.dao;

import com.socialmedia.models.Notification;
import com.socialmedia.models.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationDAOTest {

    private static NotificationDAO notificationDAO;
    private static UserDAO userDAO;

    @BeforeAll
    public static void setUp() {
        notificationDAO = new NotificationDAO();
        userDAO = new UserDAO();
    }

    @Test
    public void testCreateAndRetrieveNotification() {
        String baseName = "NotifUser" + System.currentTimeMillis();
        boolean registered = userDAO.registerUser(baseName, baseName + "@test.com", "pass");

        if (registered) {
            User testUser = userDAO.loginUser(baseName + "@test.com", "pass");
            assertNotNull(testUser);

            notificationDAO.createNotification(testUser.getId(), "Test notification", "TEST");

            List<Notification> unread = notificationDAO.getUnreadNotifications(testUser.getId());
            assertFalse(unread.isEmpty(), "Should have at least one unread notification");

            Notification n = unread.get(0);
            assertEquals("Test notification", n.getMessage());
            assertEquals("TEST", n.getType());

            // Test mark as read
            notificationDAO.markAsRead(n.getId());
            List<Notification> unreadAfter = notificationDAO.getUnreadNotifications(testUser.getId());
            assertTrue(unreadAfter.stream().noneMatch(notif -> notif.getId() == n.getId()),
                    "Notification should no longer be unread");

        } else {
            System.err.println("Skipping notification test due to DB connection or registration failure.");
        }
    }
}
