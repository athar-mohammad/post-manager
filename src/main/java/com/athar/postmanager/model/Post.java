package com.athar.postmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "Content cannot be blank")
    private String content;

    private Integer likes;        // Nullable safe type
    private Boolean deleted;      // Nullable safe type

    @Version
    private Long version;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Relationship with Comment
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // ------------------------------------------------------
    // CONSTRUCTORS
    // ------------------------------------------------------
    public Post() {
        this.likes = 0;
        this.deleted = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
        this.likes = 0;
        this.deleted = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Post(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.likes = 0;
        this.deleted = false;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // ------------------------------------------------------
    // LIFECYCLE METHODS
    // ------------------------------------------------------
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ------------------------------------------------------
    // LIKE / UNLIKE UTILITIES (safe null handling)
    // ------------------------------------------------------
    public synchronized void incrementLikes() {
        if (this.likes == null) this.likes = 0;
        this.likes++;
    }

    public synchronized void decrementLikes() {
        if (this.likes == null) this.likes = 0;
        if (this.likes > 0) {
            this.likes--;
        }
    }

    // ------------------------------------------------------
    // COMMENT MANAGEMENT
    // ------------------------------------------------------
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setPost(null);
    }

    // ------------------------------------------------------
    // SOFT DELETE UTILITIES
    // ------------------------------------------------------
    public void markDeleted() {
        this.deleted = true;
    }

    public boolean isActive() {
        return !Boolean.TRUE.equals(deleted);
    }

    // ------------------------------------------------------
    // GETTERS & SETTERS (nullable-safe types)
    // ------------------------------------------------------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }

    public String getContent() { return content; }
    public void setContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getLikes() { return likes; }
    public void setLikes(Integer likes) { this.likes = likes; }

    public Boolean isDeleted() { return deleted; }
    public void setDeleted(Boolean deleted) { this.deleted = deleted; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }
}
