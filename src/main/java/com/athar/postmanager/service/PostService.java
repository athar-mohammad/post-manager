package com.athar.postmanager.service;

import com.athar.postmanager.model.Post;
import com.athar.postmanager.model.Comment;
import com.athar.postmanager.repository.PostRepository;
import com.athar.postmanager.repository.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    // ------------------------------------------------------
    // CREATE POST
    // ------------------------------------------------------
    public Post createPost(Post post) {
        if (post.getTitle() == null || post.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (post.getContent() == null || post.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be empty");
        }
        return postRepository.save(post);
    }

    // ------------------------------------------------------
    // GET ALL POSTS
    // ------------------------------------------------------
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // ------------------------------------------------------
    // GET POST BY ID
    // ------------------------------------------------------
    public Post getPostById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        return post.orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }

    // ------------------------------------------------------
    // UPDATE POST
    // ------------------------------------------------------
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

        boolean noChange =
                existingPost.getTitle().equals(updatedPost.getTitle()) &&
                existingPost.getContent().equals(updatedPost.getContent());

        if (noChange) {
            throw new IllegalArgumentException("No changes detected to update");
        }

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        return postRepository.save(existingPost);
    }

    // ------------------------------------------------------
    // DELETE POST + COMMENT CLEANUP
    // ------------------------------------------------------
    @Transactional
    public boolean deletePost(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid post ID");
        }

        Optional<Post> existingPostOpt = postRepository.findById(id);
        if (existingPostOpt.isEmpty()) {
            return false;
        }

        Post existingPost = existingPostOpt.get();

        // Step 1: Fetch related comments (use new method)
        List<Comment> relatedComments = commentRepository.findByPost(existingPost);

        // Step 2: Delete related comments first
        if (!relatedComments.isEmpty()) {
            commentRepository.deleteAll(relatedComments);
        }

        // Step 3: Delete the post itself
        postRepository.delete(existingPost);

        return true;
    }

    // ------------------------------------------------------
    // LIKE POST
    // ------------------------------------------------------
    public Post likePost(Long id) {
        Post post = getPostById(id);
        post.setLikes(post.getLikes() + 1);
        return postRepository.save(post);
    }

    // ------------------------------------------------------
    // UNLIKE POST
    // ------------------------------------------------------
    public Post unlikePost(Long id) {
        Post post = getPostById(id);
        if (post.getLikes() > 0) {
            post.setLikes(post.getLikes() - 1);
        }
        return postRepository.save(post);
    }

    // ------------------------------------------------------
    // GET TOP LIKED POSTS
    // ------------------------------------------------------
    public List<Post> getTopLikedPosts() {
        return postRepository.findTopLikedPosts();
    }
}
