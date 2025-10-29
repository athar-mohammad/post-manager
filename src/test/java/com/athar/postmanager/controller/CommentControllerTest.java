package com.athar.postmanager.controller;

import com.athar.postmanager.model.Comment;
import com.athar.postmanager.service.CommentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentControllerTest {

    private final CommentService commentService = mock(CommentService.class);
    private final CommentController controller = new CommentController(commentService);

    @Test
    void testAddComment_Success() {
        Comment comment = new Comment(null, 1L, "Athar", "Great post!");
        when(commentService.addComment(comment)).thenReturn(new Comment(1L, 1L, "Athar", "Great post!"));

        ResponseEntity<Comment> response = controller.addComment(comment);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetCommentsByPost() {
        List<Comment> comments = Arrays.asList(new Comment(1L, 1L, "A", "Hi"));
        when(commentService.getCommentsByPost(1L)).thenReturn(comments);

        ResponseEntity<List<Comment>> response = controller.getCommentsByPost(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testDeleteComment_NotFound() {
        when(commentService.deleteComment(1L)).thenReturn(false);
        ResponseEntity<Void> response = controller.deleteComment(1L);
        assertEquals(404, response.getStatusCodeValue());
    }
}
