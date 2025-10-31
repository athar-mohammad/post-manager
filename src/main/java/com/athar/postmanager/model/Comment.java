package com.athar.postmanager.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // this enables comment.setPost(post)

    private String author;
    private String content;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Comment() {}

    public Comment(Long id, Post post, String author, String content) {
        this.id = id;
        this.post = post;
        this.author = author;
        this.content = content;
    }
    
 // Add constructor used in tests
    public Comment(Long id, Long postId, String author, String content) {
        this.id = id;
        this.post = new Post();   // create a temporary Post object for the relation
        this.post.setId(postId);
        this.author = author;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; } 

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
