package com.socialmedia.structures;

import com.socialmedia.models.Post;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PostListTest {

    @Test
    public void testAddAndGet() {
        PostList list = new PostList();
        assertEquals(0, list.size());

        Post p1 = new Post();
        p1.setId(1);
        list.add(p1);

        assertEquals(1, list.size());
        assertEquals(1, list.get(0).getId());
    }

    @Test
    public void testResize() {
        PostList list = new PostList();
        for (int i = 0; i < 15; i++) {
            Post p = new Post();
            p.setId(i);
            list.add(p);
        }
        assertEquals(15, list.size());
        assertEquals(14, list.get(14).getId());
    }

    @Test
    public void testClear() {
        PostList list = new PostList();
        list.add(new Post());
        list.clear();
        assertEquals(0, list.size());
    }
}
