package com.example.ecom.service;

import com.example.ecom.model.User;
import com.example.ecom.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    private CustomUserDetailsService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new CustomUserDetailsService(userRepository);
    }

    @Test
    void loadUserByUsername_found() {
        // Arrange: Set up the test data and mock the UserRepository's behavior.
        // Create a sample User object with a name, password, and role.
        User user = User.builder()
                .name("test")
                .email("test@email.com")
                .password("pass")
                .role("USER")
                .build();
        // Mock the UserRepository's findByEmail method to return the sample User when
        // called with "test".
        when(userRepository.findByEmail("test@email.com")).thenReturn(Optional.of(user));

        // Act: Call the loadUserByUsername method with the email "test@email.com".
        UserDetails details = service.loadUserByUsername("test@email.com");

        // Assert: Verify that the returned UserDetails object has the correct email and
        // authorities.
        // Assert that the email of the UserDetails object is equal to "test".
        assertThat(details.getUsername()).isEqualTo("test");
        // Assert that the authorities of the UserDetails object contain the "ROLE_USER"
        // authority.
        assertThat(details.getAuthorities()).extracting("authority").contains("ROLE_USER");
    }

    @Test
    void loadUserByUsername_notFound() {
        // Arrange: Set up the test data and mock the UserRepository's behavior.
        // Mock the UserRepository's findByEmail method to return an empty Optional
        // when called with "notfound".
        when(userRepository.findByEmail("notfound")).thenReturn(Optional.empty());

        // Act & Assert: Call the loadUserByUsername method with the username "notfound"
        // and assert that it throws a UsernameNotFoundException.
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("notfound"));
    }
}
