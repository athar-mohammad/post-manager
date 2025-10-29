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
    void testCreatePost_Success() {
        Post post = new Post(null, "First", "Content");
        Post savedPost = new Post(1L, "First", "Content");

        when(postRepository.save(post)).thenReturn(savedPost);

        Post result = postService.createPost(post);

        assertNotNull(result.getId());
        assertEquals("First", result.getTitle());
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void testCreatePost_EmptyTitleThrows() {
        Post post = new Post(null, "", "Content");
        assertThrows(IllegalArgumentException.class, () -> postService.createPost(post));
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
    void testUpdatePost_Success() {
        Post existing = new Post(1L, "Old Title", "Old Content");
        Post update = new Post(1L, "New Title", "New Content");

        when(postRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(postRepository.save(any(Post.class))).thenAnswer(inv -> inv.getArgument(0));

        Post result = postService.updatePost(1L, update);

        assertEquals("New Title", result.getTitle());
        verify(postRepository).save(existing);
    }

    @Test
    void testUpdatePost_NoChangesThrows() {
        Post existing = new Post(1L, "Same", "Same");
        Post update = new Post(1L, "Same", "Same");

        when(postRepository.findById(1L)).thenReturn(Optional.of(existing));

        assertThrows(IllegalArgumentException.class, () -> postService.updatePost(1L, update));
    }

    @Test
    void testDeletePost_Success() {
        Post post = new Post(1L, "Title", "Content");
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        boolean deleted = postService.deletePost(1L);

        assertTrue(deleted);
        verify(postRepository, times(1)).delete(post);
    }

    
    @Test
    void testLikePost() {
        Post post = new Post(1L, "Title", "Content");
        post.setLikes(0);

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenAnswer(inv -> inv.getArgument(0));

        Post likedPost = postService.likePost(1L);

        assertEquals(1, likedPost.getLikes());
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void testUnlikePost_WhenLikesGreaterThanZero() {
        Post post = new Post(2L, "Title", "Content");
        post.setLikes(3);

        when(postRepository.findById(2L)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenAnswer(inv -> inv.getArgument(0));

        Post unlikedPost = postService.unlikePost(2L);

        assertEquals(2, unlikedPost.getLikes());
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void testUnlikePost_WhenLikesZero_DoesNotGoNegative() {
        Post post = new Post(3L, "Title", "Content");
        post.setLikes(0);

        when(postRepository.findById(3L)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenAnswer(inv -> inv.getArgument(0));

        Post result = postService.unlikePost(3L);

        assertEquals(0, result.getLikes());
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void testGetTopLikedPosts() {
        List<Post> topLiked = Arrays.asList(
                new Post(1L, "Most Liked", "Content"),
                new Post(2L, "Second", "Content")
        );

        when(postRepository.findTopLikedPosts()).thenReturn(topLiked);

        List<Post> result = postService.getTopLikedPosts();

        assertEquals(2, result.size());
        assertEquals("Most Liked", result.get(0).getTitle());
        verify(postRepository, times(1)).findTopLikedPosts();
    }
}
