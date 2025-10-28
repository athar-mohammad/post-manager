package com.athar.postmanager.service;

import com.athar.postmanager.model.Post;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PostService {

    private final List<Post> posts = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong(1);

    public List<Post> getAllPosts() {
        return new ArrayList<>(posts);
    }

    public Post getPostById(Long id) {
        return posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Post not found"));
    }

    public Post createPost(Post post) {
        if (post.getTitle() == null || post.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        post.setId(counter.getAndIncrement());
        posts.add(post);
        return post;
    }

    public Post updatePost(Long id, Post updatedPost) {
        Post existing = getPostById(id);
        existing.setTitle(updatedPost.getTitle());
        existing.setContent(updatedPost.getContent());
        return existing;
    }

    public void deletePost(Long id) {
        posts.removeIf(p -> p.getId().equals(id));
    }
}
