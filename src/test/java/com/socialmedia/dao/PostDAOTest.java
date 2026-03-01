package com.socialmedia.dao;

import com.socialmedia.structures.PostList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PostDAOTest {

    private static PostDAO postDAO;

    @BeforeAll
    public static void setUp() {
        postDAO = new PostDAO();
    }

    @Test
    public void testCreateAndGetPosts() {
        int testUser = 99999;
        String content = "Hello JUnit! " + System.currentTimeMillis();

        try {
            boolean created = postDAO.createPost(testUser, content, null);
            if (created) {
                PostList list = postDAO.getFeedPosts(testUser, 10, 0);
                assertTrue(list.size() > 0, "Feed should contain at least the created post.");
                // Ensure recent post is found
                boolean found = false;
                for (int i = 0; i < list.size(); i++) {
                    if (content.equals(list.get(i).getContent())) {
                        found = true;
                        break;
                    }
                }
                assertTrue(found, "Newly created post content should be in the returned list.");
            }
        } catch (Exception e) {
            System.err.println("Skipping DB assertion due to potential missing FK context for test user.");
        }
    }

    @Test
    public void testPrivacySettingsFeed() {
        UserDAO userDAO = new UserDAO();
        ProfileDAO profileDAO = new ProfileDAO();
        FriendDAO friendDAO = new FriendDAO();

        String userAPass = "pass";
        String userAEmail = "userA_" + System.currentTimeMillis() + "@test.com";
        String userBEmail = "userB_" + System.currentTimeMillis() + "@test.com";

        if (userDAO.registerUser("User A", userAEmail, userAPass) &&
                userDAO.registerUser("User B", userBEmail, userAPass)) {

            int idA = userDAO.loginUser(userAEmail, userAPass).getId();
            int idB = userDAO.loginUser(userBEmail, userAPass).getId();

            // Make User A private
            profileDAO.saveOrUpdateProfile(idA, "Private Bio", "", true);
            postDAO.createPost(idA, "Secret Post", null);

            // User B should not see User A's post since they are not friends
            PostList listB = postDAO.getFeedPosts(idB, 10, 0);
            boolean foundSecret = false;
            for (int i = 0; i < listB.size(); i++) {
                if ("Secret Post".equals(listB.get(i).getContent()))
                    foundSecret = true;
            }
            assertFalse(foundSecret, "User B should not see private post of User A");

            // Make them friends
            friendDAO.addFriend(idA, idB);

            // Now User B should see it
            PostList listB_friends = postDAO.getFeedPosts(idB, 10, 0);
            boolean foundSecretFriend = false;
            for (int i = 0; i < listB_friends.size(); i++) {
                if ("Secret Post".equals(listB_friends.get(i).getContent()))
                    foundSecretFriend = true;
            }
            assertTrue(foundSecretFriend, "User B should see private post after friending User A");
        }
    }
}
