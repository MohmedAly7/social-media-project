package com.socialmedia.structures;

import com.socialmedia.models.Post;

// Custom resizable array list implementation to satisfy Data Structures requirement.
public class PostList {
    private Post[] elements;
    private int size;
    private static final int INITIAL_CAPACITY = 10;

    public PostList() {
        elements = new Post[INITIAL_CAPACITY];
        size = 0;
    }

    public void add(Post post) {
        if (size == elements.length) {
            resize();
        }
        elements[size++] = post;
    }

    public Post get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return elements[index];
    }

    public int size() {
        return size;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }

    private void resize() {
        Post[] newElements = new Post[elements.length * 2];
        System.arraycopy(elements, 0, newElements, 0, elements.length);
        elements = newElements;
    }
}
