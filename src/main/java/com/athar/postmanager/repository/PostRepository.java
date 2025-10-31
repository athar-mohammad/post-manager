package com.athar.postmanager.repository;

import com.athar.postmanager.model.Post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	/**
     * Fetches top liked posts, excluding soft-deleted ones.
     * Sorted in descending order of like count.
     */
    @Query("SELECT p FROM Post p WHERE p.deleted = false ORDER BY p.likes DESC")
    List<Post> findTopLikedPosts();

    /**
     * Fetches posts that are not marked as deleted.
     */
    @Query("SELECT p FROM Post p WHERE p.deleted = false")
    List<Post> findAllActivePosts();
}
