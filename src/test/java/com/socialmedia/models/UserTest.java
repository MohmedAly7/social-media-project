package com.socialmedia.models;

import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserGettersAndSetters() {
        User user = new User();
        user.setId(1);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPasswordHash("hashed123");

        Timestamp now = new Timestamp(System.currentTimeMillis());
        user.setCreatedAt(now);

        assertEquals(1, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("hashed123", user.getPasswordHash());
        assertEquals(now, user.getCreatedAt());
    }
}
