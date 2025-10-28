package com.athar.postmanager.repository;

import com.athar.postmanager.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // You can define custom queries later if needed
}
