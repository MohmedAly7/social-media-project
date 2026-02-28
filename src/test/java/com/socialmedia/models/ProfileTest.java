package com.socialmedia.models;

import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.*;

public class ProfileTest {

    @Test
    public void testProfileGettersAndSetters() {
        Profile profile = new Profile();
        profile.setUserId(1);
        profile.setBio("Hello world!");
        profile.setPictureUrl("/path/to/pic.jpg");

        Timestamp now = new Timestamp(System.currentTimeMillis());
        profile.setUpdatedAt(now);

        assertEquals(1, profile.getUserId());
        assertEquals("Hello world!", profile.getBio());
        assertEquals("/path/to/pic.jpg", profile.getPictureUrl());
        assertEquals(now, profile.getUpdatedAt());
    }
}
