package com.socialmedia.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordUtilsTest {

    @Test
    public void testHashPassword() {
        String password = "mySecretPassword1!";
        String hashed = PasswordUtils.hashPassword(password);

        assertNotNull(hashed);
        assertNotEquals(password, hashed);
    }

    @Test
    public void testCheckPasswordSuccess() {
        String password = "mySecretPassword2!";
        String hashed = PasswordUtils.hashPassword(password);

        assertTrue(PasswordUtils.checkPassword(password, hashed));
    }

    @Test
    public void testCheckPasswordFailure() {
        String password = "mySecretPassword3!";
        String hashed = PasswordUtils.hashPassword(password);

        assertFalse(PasswordUtils.checkPassword("wrongPassword", hashed));
    }
}
