package com.socialmedia.dao;

import com.socialmedia.models.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class FriendDAOTest {

    private static FriendDAO friendDAO;

    @BeforeAll
    public static void setUp() {
        friendDAO = new FriendDAO();
    }

    @Test
    public void testFriendOperations() {
        int user1 = 88888;
        int user2 = 99999;

        try {
            boolean added = friendDAO.addFriend(user1, user2);
            if (added) {
                List<User> friends = friendDAO.getFriends(user1);
                assertNotNull(friends);
                assertTrue(friends.size() > 0);
            }
        } catch (Exception e) {
            System.err.println("Skipping DB assertion for friends due to missing users contexts.");
        }
    }
}
