
package com.athar.postmanager.controller;

import com.athar.postmanager.model.Post;
import com.athar.postmanager.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
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
    }
}
