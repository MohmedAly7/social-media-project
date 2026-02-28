package com.socialmedia.models;

import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import static org.junit.jupiter.api.Assertions.*;

public class CommentTest {

    @Test
    public void testCommentModel() {
        Comment comment = new Comment();
        comment.setId(1);
        comment.setPostId(2);
        comment.setUserId(3);
        comment.setContent("Nice post!");
        comment.setAuthorName("Alice");

        Timestamp now = new Timestamp(System.currentTimeMillis());
        comment.setCreatedAt(now);

        assertEquals(1, comment.getId());
        assertEquals(2, comment.getPostId());
        assertEquals(3, comment.getUserId());
        assertEquals("Nice post!", comment.getContent());
        assertEquals("Alice", comment.getAuthorName());
        assertEquals(now, comment.getCreatedAt());
    }
}
