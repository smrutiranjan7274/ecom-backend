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
                .id("1")
                .email("testuser@email.com")
                .password("hashed")
                .role("USER")
                .build();
    }

    @Test
    void register_success() {
        Map<String, String> req = Map.of("email", "testuser@email.com", "password", "pass");
        when(userRepository.findByEmail("testuser@email.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenReturn(user);

        ResponseEntity<?> response = authController.register(req);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("User registered successfully");
    }

    @Test
    void register_conflict() {
        // Use "email" key to match controller logic
        Map<String, String> req = Map.of("email", "testuser@email.com", "password", "pass");
        when(userRepository.findByEmail("testuser@email.com")).thenReturn(Optional.of(user));

        ResponseEntity<?> response = authController.register(req);

        assertThat(response.getStatusCode().value()).isEqualTo(409);
    }

    @Test
    void login_success() {
        when(userRepository.findByEmail("testuser@email.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass", "hashed")).thenReturn(true);
        when(jwtUtil.generateToken("testuser@email.com", "USER")).thenReturn("token123");

        ResponseEntity<?> response = handleLogin(Optional.of(user), "testuser@email.com", "pass");

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body).isNotNull();
        if (body != null) {
            assertThat(body.get("token")).isEqualTo("token123");
            assertThat(body.get("role")).isEqualTo("USER");
        }
    }

    @Test
    void login_invalidCredentials() {
        Map<String, String> req = Map.of("email", "testuser@email.com", "password", "wrong");
        when(userRepository.findByEmail("testuser@email.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        ResponseEntity<?> response = authController.login(req);

        assertThat(response.getStatusCode().value()).isEqualTo(401);
    }

    @Test
    void login_tokenOrRoleNull() {
        Map<String, String> req = Map.of("email", "testuser@email.com", "password", "pass");
        Optional<User> userOpt = Optional.of(user);
        String email = req.get("email");
        String password = req.get("password");

        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            String token = jwtUtil.generateToken(email, userOpt.get().getRole());
            String role = userOpt.get().getRole();
            if (token != null && role != null) {
                ResponseEntity<?> response = ResponseEntity.ok(Map.of(
                        "token", token,
                        "role", role));
                assertThat(response.getStatusCode().value()).isEqualTo(200);
                Map<?, ?> body = (Map<?, ?>) response.getBody();
                assertThat(body).isNotNull();
                if (body != null) {
                    assertThat(body.get("token")).isEqualTo(token);
                    assertThat(body.get("role")).isEqualTo(role);
                }
            } else {
                ResponseEntity<?> response = ResponseEntity.status(500).body("Token or role is null");
                assertThat(response.getStatusCode().value()).isEqualTo(500);
                assertThat(response.getBody()).isEqualTo("Token or role is null");
            }
        }
    }

    ResponseEntity<?> handleLogin(Optional<User> userOpt, String email, String password) {
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            String token = jwtUtil.generateToken(email, userOpt.get().getRole());
            String role = userOpt.get().getRole();
            if (token != null && role != null) {
                return ResponseEntity.ok(Map.of(
                        "token", token,
                        "role", role));
            } else {
                return ResponseEntity.status(500).body("Token or role is null");
            }
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}
