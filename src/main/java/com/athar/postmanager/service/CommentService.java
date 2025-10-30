package com.athar.postmanager.service;

import com.athar.postmanager.model.Comment;
import com.athar.postmanager.repository.CommentRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
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
        PageRequest pageRequest = PageRequest.of(page, size);
        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId, pageRequest).getContent();
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
