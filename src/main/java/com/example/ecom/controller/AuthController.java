package com.example.ecom.controller;

import com.example.ecom.model.User;
import com.example.ecom.repository.UserRepository;
import com.example.ecom.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> req) {
        if (userRepository.findByEmail(req.get("email")).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already registered");
        }
        User user = User.builder()
                .name(req.get("name"))
                // .phone(Long.parseLong(req.get("phone")))
                .email(req.get("email"))
                .password(passwordEncoder.encode(req.get("password")))
                .role(req.getOrDefault("role", "USER"))
                .build();
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        User user = userRepository.findByEmail(req.get("email"))
                .orElse(null);
        if (user == null || !passwordEncoder.matches(req.get("password"), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email credentials");
        }
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return ResponseEntity.ok(Map.of("token", token, "role", user.getRole(), "email", user.getEmail(), "name",
                user.getName(), "id", user.getId()));
    }

    @GetMapping("/admin")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}