package com.athar.postmanager.service;

import com.athar.postmanager.model.Post;
import com.athar.postmanager.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // Create new post
    public Post createPost(Post post) {
        if (post.getTitle() == null || post.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (post.getContent() == null || post.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }
        return postRepository.save(post);
    }

    // Get all posts
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // Get post by ID
    public Post getPostById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            return post.get();
        } else {
            throw new IllegalArgumentException("Post not found");
        }
    }

    // Update post
    public Post updatePost(Long id, Post updatedPost) {
        Optional<Post> existingPostOpt = postRepository.findById(id);
        if (existingPostOpt.isEmpty()) {
            throw new IllegalArgumentException("Post not found");
        }

        Post existingPost = existingPostOpt.get();

        if (updatedPost.getTitle() == null || updatedPost.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (updatedPost.getContent() == null || updatedPost.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }

        boolean noChange = existingPost.getTitle().equals(updatedPost.getTitle())
                && existingPost.getContent().equals(updatedPost.getContent());

        if (noChange) {
            throw new IllegalArgumentException("No changes detected to update");
        }

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        return postRepository.save(existingPost);
    }

    // Delete post
    public boolean deletePost(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid post ID");
        }

        Optional<Post> existingPost = postRepository.findById(id);
        if (existingPost.isPresent()) {
            postRepository.delete(existingPost.get());
            return true;
        }
        return false;
    }

    // New Feature: Like a post
    public Post likePost(Long id) {
        Post post = getPostById(id);
        post.setLikes(post.getLikes() + 1);
        return postRepository.save(post);
    }

    // New Feature: Unlike a post
    public Post unlikePost(Long id) {
        Post post = getPostById(id);
        if (post.getLikes() > 0) {
            post.setLikes(post.getLikes() - 1);
        }
        return postRepository.save(post);
    }

    // New Feature: Get top liked posts
    public List<Post> getTopLikedPosts() {
        return postRepository.findTopLikedPosts();
    }
}
