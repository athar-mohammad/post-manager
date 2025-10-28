package com.athar.postmanager.service;

import com.athar.postmanager.model.Post;
import com.athar.postmanager.repository.PostRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePost() {
        Post post = new Post(null, "First", "Content");
        Post savedPost = new Post(1L, "First", "Content");

        when(postRepository.save(post)).thenReturn(savedPost);

        Post result = postService.createPost(post);

        assertNotNull(result.getId());
        assertEquals("First", result.getTitle());
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void testGetAllPosts() {
        List<Post> mockPosts = Arrays.asList(
                new Post(1L, "A", "B"),
                new Post(2L, "C", "D")
        );

        when(postRepository.findAll()).thenReturn(mockPosts);

        List<Post> result = postService.getAllPosts();

        assertEquals(2, result.size());
        verify(postRepository, times(1)).findAll();
    }

    @Test
    void testDeletePost() {
        Long postId = 1L;
        Post post = new Post(postId, "Title", "Content");

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        postService.deletePost(postId);

        verify(postRepository, times(1)).delete(post);
    }
}
