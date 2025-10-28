package com.athar.postmanager.service;

import com.athar.postmanager.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class PostServiceTest {

    private PostService postService;

    @BeforeEach
    void setup() {
        postService = new PostService();
    }

    @Test
    void testCreatePost() {
        Post post = new Post(null, "First", "Content");
        Post saved = postService.createPost(post);
        assertNotNull(saved.getId());
        assertEquals("First", saved.getTitle());
    }

    @Test
    void testGetAllPosts() {
        postService.createPost(new Post(null, "A", "B"));
        List<Post> posts = postService.getAllPosts();
        assertFalse(posts.isEmpty());
    }

    @Test
    void testDeletePost() {
        Post p = postService.createPost(new Post(null, "A", "B"));
        postService.deletePost(p.getId());
        assertTrue(postService.getAllPosts().isEmpty());
    }
}
