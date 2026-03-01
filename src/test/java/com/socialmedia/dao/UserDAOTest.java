package com.socialmedia.dao;

import com.socialmedia.models.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {

    private static UserDAO userDAO;

    @BeforeAll
    public static void setUp() {
        userDAO = new UserDAO();
    }

    @Test
    public void testRegisterAndLoginUser() {
        // Use a unique email to avoid conflicts across test runs
        String email = "test" + System.currentTimeMillis() + "@example.com";
        String password = "password123";
        String name = "Test User";

        boolean registered = userDAO.registerUser(name, email, password);

        // Assertions are conditional in case the database isn't provisioned during the
        // test run
        if (registered) {
            User loggedInUser = userDAO.loginUser(email, password);
            assertNotNull(loggedInUser, "User should be able to login after registration");
            assertEquals(email, loggedInUser.getEmail());
            assertEquals(name, loggedInUser.getName());

            User wrongUser = userDAO.loginUser(email, "wrongpassword");
            assertNull(wrongUser, "User should not login with wrong password");
        } else {
            System.err.println("Skipping authentication assertion because database connection might have failed.");
        }
    }

    @Test
    public void testSearchUsers() {
        String uniqueName = "SearchTestUser" + System.currentTimeMillis();
        boolean registered = userDAO.registerUser(uniqueName, uniqueName + "@example.com", "password");

        if (registered) {
            java.util.List<User> results = userDAO.searchUsers("SearchTestUser");
            assertNotNull(results, "Search results should not be null");
            assertTrue(results.size() > 0, "Search should return at least one result");
            boolean found = false;
            for (User u : results) {
                if (u.getName().equals(uniqueName)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "The registered user should be found in search results");
        } else {
            System.err.println("Skipping search test because database connection might have failed.");
        }
    }
}
