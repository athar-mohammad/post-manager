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

    /**
     * Positive (P2P) test: Delete existing post should return 204 No Content
     */
    @Test
    public void testDeleteExistingPost_ShouldReturnSuccess() {
        // Arrange
        Post post = new Post("Test Title", "Test Content");
        Post saved = postRepository.save(post);

        // Act
        ResponseEntity<String> response = restTemplate.exchange(
            "/api/posts/" + saved.getId(),
            HttpMethod.DELETE,
            null,
            String.class
        );

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertTrue(postRepository.findById(saved.getId()).isEmpty(), "Post should be deleted from DB");
    }

    /**
     * Negative (F2P) test: Delete non-existing post should return 404 Not Found
     */
    @Test
    public void testDeleteNonExistingPost_ShouldReturnNotFound() {
        // Act
        ResponseEntity<String> response = restTemplate.exchange(
            "/api/posts/99999",
            HttpMethod.DELETE,
            HttpEntity.EMPTY,
            String.class
        );

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("Post not found"), "Should return proper error message");
    }

    /**
     * Positive (P2P) test: Create and delete flow verification
     */
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
