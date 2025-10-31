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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository; // ✅ Added mock

    @InjectMocks
    private CommentService commentService;

    private Post mockPost;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockPost = new Post(1L, "Hello", "World");
    }

    @Test
    void testAddComment_Success() {
        Comment comment = new Comment(null, mockPost, "Athar", "Nice post!");
        when(commentRepository.save(comment))
                .thenReturn(new Comment(1L, mockPost, "Athar", "Nice post!"));

        Comment saved = commentService.addComment(comment);

        assertNotNull(saved);
        verify(commentRepository).save(comment);
    }

    @Test
    void testAddComment_EmptyContentThrows() {
        Comment comment = new Comment(null, mockPost, "Athar", "");
        assertThrows(IllegalArgumentException.class, () -> commentService.addComment(comment));
    }

    @Test
    void testGetCommentsByPost_Paginated() {
        when(postRepository.findById(mockPost.getId())).thenReturn(Optional.of(mockPost)); // ✅ Added

        List<Comment> mockComments = Arrays.asList(
                new Comment(1L, mockPost, "User1", "Nice!"),
                new Comment(2L, mockPost, "User2", "Wow!")
        );

        Page<Comment> mockPage = new PageImpl<>(mockComments);
        when(commentRepository.findByPostOrderByCreatedAtDesc(eq(mockPost), any(PageRequest.class)))
                .thenReturn(mockPage);

        List<Comment> result = commentService.getCommentsByPost(mockPost.getId(), 0, 5);

        assertEquals(2, result.size());
        verify(commentRepository).findByPostOrderByCreatedAtDesc(eq(mockPost), any(PageRequest.class));
    }

    @Test
    void testDeleteComment_Success() {
        Comment comment = new Comment(1L, mockPost, "A", "Hi");
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        boolean deleted = commentService.deleteComment(1L);

        assertTrue(deleted);
        verify(commentRepository).delete(comment);
    }

    @Test
    void testDeleteComment_NotFound() {
        when(commentRepository.findById(99L)).thenReturn(Optional.empty());
        boolean deleted = commentService.deleteComment(99L);
        assertFalse(deleted);
    }
}
