package com.socialmedia.models;

import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.*;

public class PostTest {

    @Test
    public void testPostGettersAndSetters() {
        Post post = new Post();
        post.setId(1);
        post.setUserId(2);
        post.setContent("This is a test post!");
        post.setImageUrl("/path/img.png");
        post.setAuthorName("Jane Doe");

        Timestamp now = new Timestamp(System.currentTimeMillis());
        post.setCreatedAt(now);

        assertEquals(1, post.getId());
        assertEquals(2, post.getUserId());
        assertEquals("This is a test post!", post.getContent());
        assertEquals("/path/img.png", post.getImageUrl());
        assertEquals("Jane Doe", post.getAuthorName());
        assertEquals(now, post.getCreatedAt());
    }
}
