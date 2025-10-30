package com.athar.postmanager.service;

import com.athar.postmanager.model.User;
import com.athar.postmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_Success() {
        when(userRepository.existsByUsername("athar")).thenReturn(false);
        when(userRepository.save(any(User.class)))
                .thenReturn(new User(1L, "athar", "encoded", Set.of("USER")));

        User user = authService.register("athar", "12345");
        assertEquals("athar", user.getUsername());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegister_UsernameExists() {
        when(userRepository.existsByUsername("athar")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> authService.register("athar", "pass"));
    }

    @Test
    void testLogin_Success() {
        User mockUser = new User(1L, "athar", new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("12345"), Set.of("USER"));
        when(userRepository.findByUsername("athar")).thenReturn(Optional.of(mockUser));

        User result = authService.login("athar", "12345");
        assertEquals("athar", result.getUsername());
    }

    @Test
    void testLogin_InvalidPassword() {
        User mockUser = new User(1L, "athar", new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("12345"), Set.of("USER"));
        when(userRepository.findByUsername("athar")).thenReturn(Optional.of(mockUser));

        assertThrows(IllegalArgumentException.class, () -> authService.login("athar", "wrongpass"));
    }
}
