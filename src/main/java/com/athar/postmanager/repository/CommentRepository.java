package com.athar.postmanager.repository;

import com.athar.postmanager.model.Comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
    
    Page<Comment> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);
}
