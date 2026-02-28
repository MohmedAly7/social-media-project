package com.socialmedia.models;

import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.*;

public class LikeTest {

    @Test
    public void testLikeModel() {
        Like like = new Like();
        like.setPostId(1);
        like.setUserId(2);

        Timestamp now = new Timestamp(System.currentTimeMillis());
        like.setCreatedAt(now);

        assertEquals(1, like.getPostId());
        assertEquals(2, like.getUserId());
        assertEquals(now, like.getCreatedAt());
    }
}
