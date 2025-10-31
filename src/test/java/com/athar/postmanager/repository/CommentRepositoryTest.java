package com.athar.postmanager.repository;

import com.athar.postmanager.model.Comment;
import com.athar.postmanager.model.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    void testDeleteByPostRemovesAllComments() {
        Post post = new Post(null, "Hello", "World");
        post = postRepository.save(post);

        commentRepository.save(new Comment(null, post, "A", "First"));
        commentRepository.save(new Comment(null, post, "B", "Second"));

        commentRepository.deleteByPost(post);

        List<Comment> remaining = commentRepository.findByPost(post);
        assertThat(remaining).isEmpty();
    }
}
