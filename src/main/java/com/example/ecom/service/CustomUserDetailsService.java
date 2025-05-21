package com.example.ecom.service;

import com.example.ecom.model.User;
import com.example.ecom.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Custom implementation of UserDetailsService for Spring Security.
 * Loads user-specific data from the database for authentication and
 * authorization.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Constructor for dependency injection of UserRepository.
     *
     * @param userRepository the repository to access user data
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads a user by username for authentication.
     *
     * @param username the username to search for
     * @return UserDetails object containing user information and authorities
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // Create a UserDetails object with email, password, and role-based authority
        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase())));
    }
}
