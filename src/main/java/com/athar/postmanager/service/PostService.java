package com.athar.postmanager.service;

import com.athar.postmanager.exception.ResourceNotFoundException;
import com.athar.postmanager.model.Post;
import com.athar.postmanager.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

	@Autowired
	private PostRepository postRepository;

	public List<Post> getAllPosts() {
		return postRepository.findAll();
	}

	public Post getPostById(Long id) {
		return postRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + id));
	}

	public Post createPost(Post post) {
		if (post.getTitle() == null || post.getTitle().isEmpty()) {
			throw new IllegalArgumentException("Title cannot be empty");
		}
		return postRepository.save(post);
	}

	public Post updatePost(Long id, Post updatedPost) {
		Post existing = getPostById(id);
		existing.setTitle(updatedPost.getTitle());
		existing.setContent(updatedPost.getContent());
		return postRepository.save(existing);
	}

	public boolean deletePost(Long id) {
	    Optional<Post> existingPost = postRepository.findById(id);
	    if (existingPost.isPresent()) {
	        postRepository.delete(existingPost.get());
	        return true;
	    } else {
	        return false;
	    }
	}
}
