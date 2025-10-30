package com.athar.postmanager.service;

import com.athar.postmanager.model.Comment;
import com.athar.postmanager.repository.CommentRepository;
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

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ✅ Test: Adding a comment successfully
    @Test
    void testAddComment_Success() {
        Comment comment = new Comment(null, 1L, "Athar", "Nice post!");
        when(commentRepository.save(comment)).thenReturn(new Comment(1L, 1L, "Athar", "Nice post!"));

        Comment saved = commentService.addComment(comment);

        assertNotNull(saved);
        verify(commentRepository).save(comment);
    }

    // ✅ Test: Adding an invalid (empty) comment throws exception
    @Test
    void testAddComment_EmptyContentThrows() {
        Comment comment = new Comment(null, 1L, "Athar", "");
        assertThrows(IllegalArgumentException.class, () -> commentService.addComment(comment));
    }

    // ✅ Test: Fetching comments by post (with pagination)
    @Test
    void testGetCommentsByPost_Paginated() {
        List<Comment> mockComments = Arrays.asList(
                new Comment(1L, 1L, "User1", "Nice!"),
                new Comment(2L, 1L, "User2", "Wow!")
        );

        Page<Comment> mockPage = new PageImpl<>(mockComments);
        when(commentRepository.findByPostIdOrderByCreatedAtDesc(1L, PageRequest.of(0, 5)))
                .thenReturn(mockPage);

        List<Comment> result = commentService.getCommentsByPost(1L, 0, 5);
        assertEquals(2, result.size());
        verify(commentRepository).findByPostIdOrderByCreatedAtDesc(1L, PageRequest.of(0, 5));
    }

    // ✅ Test: Deleting a comment successfully
    @Test
    void testDeleteComment_Success() {
        Comment comment = new Comment(1L, 1L, "A", "Hi");
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        boolean deleted = commentService.deleteComment(1L);

        assertTrue(deleted);
        verify(commentRepository).delete(comment);
    }

    // ✅ Test: Deleting a non-existent comment returns false
    @Test
    void testDeleteComment_NotFound() {
        when(commentRepository.findById(99L)).thenReturn(Optional.empty());
        boolean deleted = commentService.deleteComment(99L);
        assertFalse(deleted);
    }
}
