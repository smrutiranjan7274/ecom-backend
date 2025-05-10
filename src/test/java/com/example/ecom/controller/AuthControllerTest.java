package com.example.ecom.controller;

import com.example.ecom.model.User;
import com.example.ecom.repository.UserRepository;
import com.example.ecom.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder()
                .id(1L)
                .username("testuser")
                .password("hashed")
                .role("USER")
                .build();
    }

    @Test
    void register_success() {
        // Arrange: Set up the test data and mock the necessary dependencies.
        // Create a map representing the registration request with username and password.
        Map<String, String> req = Map.of("username", "testuser", "password", "pass");
        // Mock the userRepository to return an empty Optional when findByUsername("testuser") is called, indicating that the user does not exist.
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        // Mock the passwordEncoder to return "hashed" when encode("pass") is called, simulating password hashing.
        when(passwordEncoder.encode("pass")).thenReturn("hashed");
        // Mock the userRepository to return the user object when save(any(User.class)) is called, simulating successful user saving.
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act: Call the register(req) method on the authController.
        ResponseEntity<?> response = authController.register(req);

        // Assert: Verify that the registration was successful and the response is as expected.
        // Assert that the response status code is 200 (OK).
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        // Assert that the response body is "User registered successfully".
        assertThat(response.getBody()).isEqualTo("User registered successfully");
    }

    @Test
    void register_conflict() {
        // Arrange: Set up the test data and mock the userRepository to simulate a user already existing.
        // Create a map representing the registration request with username and password.
        Map<String, String> req = Map.of("username", "testuser", "password", "pass");
        // Mock the userRepository to return an Optional containing the user object when findByUsername("testuser") is called, indicating that the user already exists.
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Act: Call the register(req) method on the authController.
        ResponseEntity<?> response = authController.register(req);

        // Assert: Verify that the registration failed due to a conflict and the response is as expected.
        // Assert that the response status code is 409 (Conflict).
        assertThat(response.getStatusCode().value()).isEqualTo(409);
    }

    @SuppressWarnings("null")
    @Test
    void login_success() {
        // Arrange: Set up the test data and mock the necessary dependencies for a successful login.
        // Create a map representing the login request with username and password.
        Map<String, String> req = Map.of("username", "testuser", "password", "pass");
        // Mock the userRepository to return an Optional containing the user object when findByUsername("testuser") is called, simulating a user found in the database.
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        // Mock the passwordEncoder to return true when matches("pass", "hashed") is called, simulating a successful password match.
        when(passwordEncoder.matches("pass", "hashed")).thenReturn(true);
        // Mock the jwtUtil to return "token123" when generateToken("testuser", "USER") is called, simulating JWT generation.
        when(jwtUtil.generateToken("testuser", "USER")).thenReturn("token123");

        // Act: Call the login(req) method on the authController.
        ResponseEntity<?> response = authController.login(req);

        // Assert: Verify that the login was successful and the response contains the expected token and role.
        // Assert that the response status code is 200 (OK).
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        // Assert that the response body is not null.
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body).isNotNull();
        // Assert that the response body contains the token "token123".
        assertThat(body.get("token")).isEqualTo("token123");
        // Assert that the response body contains the role "USER".
        assertThat(body.get("role")).isEqualTo("USER");
    }

    @Test
    void login_invalidCredentials() {
        // Arrange: Set up the test data and mock the necessary dependencies for an unsuccessful login due to invalid credentials.
        // Create a map representing the login request with an incorrect password.
        Map<String, String> req = Map.of("username", "testuser", "password", "wrong");
        // Mock the userRepository to return an Optional containing the user object when findByUsername("testuser") is called, simulating a user found in the database.
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        // Mock the passwordEncoder to return false when matches("wrong", "hashed") is called, simulating an incorrect password.
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        // Act: Call the login(req) method on the authController.
        ResponseEntity<?> response = authController.login(req);

        // Assert: Verify that the login failed due to invalid credentials and the response status code is 401 (Unauthorized).
        assertThat(response.getStatusCode().value()).isEqualTo(401);
    }
}
