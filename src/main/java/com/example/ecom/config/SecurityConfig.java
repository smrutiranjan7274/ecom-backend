package com.example.ecom.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.ecom.security.JwtAuthFilter;

/**
 * Security configuration for the application.
 *
 * Sets up HTTP security, endpoint access rules, and password encoding.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Injects the application's UserDetailsService for authentication
    private final UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    /**
     * Constructor for dependency injection of UserDetailsService.
     *
     * @param userDetailsService the service to load user-specific data
     */
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configures the security filter chain for HTTP requests.
     *
     * - Disables frame options for H2 console access (for development).
     * - Disables CSRF protection (stateless JWT-based security).
     * - Sets endpoint access rules:
     *      - "/h2-console/**", "/api/auth/login", and "/api/auth/register" are public.
     *      - "/api/products/**" is public (product browsing).
     *      - "/api/auth/admin" requires the ADMIN role.
     *      - All other "/api/**" endpoints require authentication.
     *      - Any other requests also require authentication.
     * - Registers the custom UserDetailsService for user lookup.
     * - Adds the JWT authentication filter before the standard username/password filter.
     *
     * @param http the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                // Public endpoints (H2 console, login, registration)
                .requestMatchers("/h2-console/**", "/api/auth/login", "/api/auth/register").permitAll()
                // Public product endpoints
                .requestMatchers("/api/products/**").permitAll()
                // Admin-only endpoint
                .requestMatchers("/api/auth/admin").hasRole("ADMIN")
                // For future: admin and seller endpoints
                // .requestMatchers("/api/admin/**").hasRole("ADMIN")
                // .requestMatchers("/api/seller/**").hasAnyRole("SELLER", "ADMIN")
                // All other API endpoints require authentication
                .requestMatchers("/api/**").authenticated()
                // Any other requests require authentication
                .anyRequest().authenticated())
            .userDetailsService(userDetailsService)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /**
     * Bean for password encoding using BCrypt.
     *
     * @return a BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}