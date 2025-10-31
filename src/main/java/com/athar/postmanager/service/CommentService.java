package com.athar.postmanager.service;

import com.athar.postmanager.model.Comment;
import com.athar.postmanager.model.Post;
import com.athar.postmanager.repository.CommentRepository;
import com.athar.postmanager.repository.PostRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public Comment addComment(Comment comment) {
        if (comment.getAuthor() == null || comment.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Author cannot be empty");
        }
        if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByPost(Long postId, int page, int size) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        return commentRepository.findByPostOrderByCreatedAtDesc(post, PageRequest.of(page, size))
                .getContent();
    }
    
    public boolean deleteComment(Long id) {
        Optional<Comment> existing = commentRepository.findById(id);
        if (existing.isPresent()) {
            commentRepository.delete(existing.get());
            return true;
        }
        return false;
    }
}
