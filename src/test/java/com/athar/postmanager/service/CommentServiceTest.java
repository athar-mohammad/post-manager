package com.athar.postmanager.service;

import com.athar.postmanager.model.Comment;
import com.athar.postmanager.repository.CommentRepository;
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

public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddComment_Success() {
        Comment comment = new Comment(null, 1L, "Athar", "Nice post!");
        when(commentRepository.save(comment)).thenReturn(new Comment(1L, 1L, "Athar", "Nice post!"));

        Comment saved = commentService.addComment(comment);
        assertNotNull(saved);
        verify(commentRepository).save(comment);
    }

    @Test
    void testAddComment_EmptyContentThrows() {
        Comment comment = new Comment(null, 1L, "Athar", "");
        assertThrows(IllegalArgumentException.class, () -> commentService.addComment(comment));
    }

    @Test
    void testGetCommentsByPost() {
        List<Comment> mockComments = Arrays.asList(
                new Comment(1L, 1L, "A", "Nice!"),
                new Comment(2L, 1L, "B", "Wow!")
        );
        when(commentRepository.findByPostId(1L)).thenReturn(mockComments);

        List<Comment> result = commentService.getCommentsByPost(1L);
        assertEquals(2, result.size());
    }

    @Test
    void testDeleteComment_Success() {
        Comment comment = new Comment(1L, 1L, "A", "Hi");
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        boolean deleted = commentService.deleteComment(1L);
        assertTrue(deleted);
        verify(commentRepository).delete(comment);
    }
}
