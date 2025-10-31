package com.athar.postmanager.repository;

import com.athar.postmanager.model.Comment;
import com.athar.postmanager.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Fetch all comments for a post (non-paginated)
    List<Comment> findByPost(Post post);

    // Fetch comments with pagination, ordered by creation time (newest first)
    Page<Comment> findByPostOrderByCreatedAtDesc(Post post, Pageable pageable);

    // Delete all comments belonging to a specific post
    void deleteByPost(Post post);
    
    void deleteAllByPostIsNull();
}
