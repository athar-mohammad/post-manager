package com.athar.postmanager.service;

import com.athar.postmanager.model.Comment;
import com.athar.postmanager.model.Post;
import com.athar.postmanager.repository.CommentRepository;
import com.athar.postmanager.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private PostService postService;

    private Post mockPost;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockPost = new Post(1L, "Title", "Content");
    }

    // --------------------------------------------------------------------
    // Create Post Tests
    // --------------------------------------------------------------------
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

    // --------------------------------------------------------------------
    // Get All Posts
    // --------------------------------------------------------------------
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

    // --------------------------------------------------------------------
    // Update Post
    // --------------------------------------------------------------------
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

    // --------------------------------------------------------------------
    // Delete Post + Comment Cleanup Tests
    // --------------------------------------------------------------------
    @Test
    void testDeletePost_WithComments_Success() {
        Post post = new Post(1L, "Title", "Content");
        List<Comment> comments = Arrays.asList(
                new Comment(1L, post, "A", "C1"),
                new Comment(2L, post, "B", "C2")
        );

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentRepository.findByPost(post)).thenReturn(comments);

        boolean deleted = postService.deletePost(1L);

        assertTrue(deleted);
        verify(commentRepository, times(1)).findByPost(post);
        verify(commentRepository, times(1)).deleteAll(comments);
        verify(postRepository, times(1)).delete(post);
    }

    @Test
    void testDeletePost_NoComments_Success() {
        Post post = new Post(2L, "T", "C");
        when(postRepository.findById(2L)).thenReturn(Optional.of(post));
        when(commentRepository.findByPost(post)).thenReturn(Collections.emptyList());

        boolean deleted = postService.deletePost(2L);

        assertTrue(deleted);
        verify(commentRepository, times(1)).findByPost(post);
        verify(commentRepository, never()).deleteAll(anyList());
        verify(postRepository, times(1)).delete(post);
    }

    @Test
    void testDeletePost_InvalidIdThrows() {
        assertThrows(IllegalArgumentException.class, () -> postService.deletePost(null));
    }

    @Test
    void testDeletePost_PostNotFound_ReturnsFalse() {
        when(postRepository.findById(99L)).thenReturn(Optional.empty());
        boolean result = postService.deletePost(99L);
        assertFalse(result);
        verify(postRepository, times(1)).findById(99L);
    }

    // --------------------------------------------------------------------
    // Like/Unlike Post Tests
    // --------------------------------------------------------------------
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

    // --------------------------------------------------------------------
    // Top Liked Posts
    // --------------------------------------------------------------------
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

    // --------------------------------------------------------------------
    // F2P Test: Failure during Comment Deletion rolls back post delete
    // --------------------------------------------------------------------
    @Test
    void testDeletePost_CommentDeletionFails_ShouldRollback() {
        Post post = new Post(5L, "Rollback", "Test");
        List<Comment> comments = Arrays.asList(new Comment(10L, post, "A", "Bad"));

        when(postRepository.findById(5L)).thenReturn(Optional.of(post));
        when(commentRepository.findByPost(post)).thenReturn(comments);
        doThrow(new RuntimeException("DB failure")).when(commentRepository).deleteAll(comments);

        assertThrows(RuntimeException.class, () -> postService.deletePost(5L));

        // ensure post is NOT deleted due to rollback
        verify(postRepository, never()).delete(post);
    }
    
    
    @Test
    void testLikePost_PostNotFound_ThrowsException() {
        when(postRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> postService.likePost(100L));
        verify(postRepository, times(1)).findById(100L);
        verify(postRepository, never()).save(any());
    }

    @Test
    void testUnlikePost_PostNotFound_ThrowsException() {
        when(postRepository.findById(200L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> postService.unlikePost(200L));
        verify(postRepository, times(1)).findById(200L);
        verify(postRepository, never()).save(any());
    }

    @Test
    void testDeletePost_WhenCommentListIsNull_ShouldHandleGracefully() {
        Post post = new Post(7L, "Null Comment Case", "Content");
        when(postRepository.findById(7L)).thenReturn(Optional.of(post));
        when(commentRepository.findByPost(post)).thenReturn(null); // simulate null instead of empty list

        boolean deleted = postService.deletePost(7L);

        assertTrue(deleted);
        verify(commentRepository, times(1)).findByPost(post);
        verify(postRepository, times(1)).delete(post);
    }

    @Test
    void testLikeAndUnlike_SequenceFlow() {
        Post post = new Post(8L, "Seq", "Flow");
        post.setLikes(0);

        when(postRepository.findById(8L)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenAnswer(inv -> inv.getArgument(0));

        postService.likePost(8L);
        postService.likePost(8L);
        postService.unlikePost(8L);

        assertEquals(1, post.getLikes()); // 0 -> 1 -> 2 -> 1
        verify(postRepository, atLeast(3)).save(post);
    }
}
