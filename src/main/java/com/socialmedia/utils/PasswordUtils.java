package com.socialmedia.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordUtils {

    // Simple hashing using SHA-256 for educational project scenario.
    public static String hashPassword(String password) {
        if (password == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Algorithm not found", e);
        }
    }

    public static boolean checkPassword(String password, String hashed) {
        if (password == null || hashed == null) {
            return false;
        }
        String computed = hashPassword(password);
        return computed != null && computed.equals(hashed);
    }
}
