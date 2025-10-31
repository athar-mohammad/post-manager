package com.athar.postmanager.controller;

import com.athar.postmanager.model.Comment;
import com.athar.postmanager.model.Post;
import com.athar.postmanager.repository.CommentRepository;
import com.athar.postmanager.repository.PostRepository;
import com.athar.postmanager.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    private Post savedPost;

    @BeforeEach
    void setup() {
        commentRepository.deleteAll();
        postRepository.deleteAll();

        // Create and persist a Post (so foreign key constraints are valid)
        Post post = new Post();
        post.setTitle("Integration Test Post");
        post.setContent("Post for testing comments");
        savedPost = postRepository.save(post);

        // Add 10 comments linked to this persisted Post
        for (int i = 1; i <= 10; i++) {
            Comment comment = new Comment();
            comment.setPost(savedPost);
            comment.setAuthor("User" + i);
            comment.setContent("Comment " + i);
            comment.setCreatedAt(LocalDateTime.now().minusMinutes(i));
            commentRepository.save(comment);
        }
    }

    // Integration Test: Verify pagination and order
    @Test
    void testPaginationAndOrder() throws Exception {
        Page<Comment> page = commentRepository.findByPostOrderByCreatedAtDesc(savedPost, PageRequest.of(0, 5));
        assertThat(page.getContent().size()).isEqualTo(5);
        assertThat(page.getContent().get(0).getCreatedAt()).isAfter(page.getContent().get(4).getCreatedAt());

        mockMvc.perform(get("/api/comments")
                        .param("postId", savedPost.getId().toString())
                        .param("page", "0")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // -----------------------------------------------------
    // Unit Test Section (Mockito-based)
    // -----------------------------------------------------

    private final CommentService commentService = mock(CommentService.class);
    private final CommentController controller = new CommentController(commentService);

    @Test
    void testAddComment_Success() {
        Comment comment = new Comment();
        comment.setAuthor("Athar");
        comment.setContent("Great post!");
        comment.setPost(savedPost);

        Comment savedComment = new Comment();
        savedComment.setId(1L);
        savedComment.setAuthor("Athar");
        savedComment.setContent("Great post!");
        savedComment.setPost(savedPost);

        when(commentService.addComment(comment)).thenReturn(savedComment);

        ResponseEntity<Comment> response = controller.addComment(comment);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Great post!", response.getBody().getContent());
    }

    @Test
    void testGetCommentsByPost() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setAuthor("A");
        comment.setContent("Hi");
        comment.setPost(savedPost);

        List<Comment> comments = Arrays.asList(comment);
        when(commentService.getCommentsByPost(savedPost.getId(), 0, 5)).thenReturn(comments);

        ResponseEntity<List<Comment>> response = controller.getCommentsByPost(savedPost.getId(), 0, 5);
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
