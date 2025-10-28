package com.athar.postmanager.controller;

import com.athar.postmanager.model.Post;
import com.athar.postmanager.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PostControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    public void setup() {
        // Clear database before each test to ensure isolation
        postRepository.deleteAll();
    }

    @Test
    public void testDeleteExistingPost_ShouldReturnNoContent() {
        Post post = postRepository.save(new Post("Sample", "Content"));
        ResponseEntity<String> response = restTemplate.exchange(
            "/api/posts/" + post.getId(),
            HttpMethod.DELETE, null, String.class
        );
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteNonExistingPost_ShouldReturnNotFound() {
        ResponseEntity<String> response = restTemplate.exchange(
            "/api/posts/9999",
            HttpMethod.DELETE, null, String.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteThenFetch_ShouldReturnNotFound() {
        Post post = postRepository.save(new Post("Title", "Content"));
        restTemplate.exchange("/api/posts/" + post.getId(), HttpMethod.DELETE, null, String.class);
        ResponseEntity<String> getResponse = restTemplate.getForEntity("/api/posts/" + post.getId(), String.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    
    @Test
    public void testCreateAndDeleteFlow() {
        // Arrange - create a post first
        Post newPost = new Post("Flow Test Title", "Flow Test Content");
        ResponseEntity<Post> createdResponse = restTemplate.postForEntity("/api/posts", newPost, Post.class);
        assertEquals(HttpStatus.CREATED, createdResponse.getStatusCode());

        Long postId = createdResponse.getBody().getId();

        // Act - delete that post
        ResponseEntity<String> deleteResponse = restTemplate.exchange(
            "/api/posts/" + postId,
            HttpMethod.DELETE,
            HttpEntity.EMPTY,
            String.class
        );

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
        assertTrue(postRepository.findById(postId).isEmpty(), "Post should be deleted successfully");
    }
}
