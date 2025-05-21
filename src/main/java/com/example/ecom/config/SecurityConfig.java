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
import com.example.ecom.security.CustomAuthEntryPoint;
import com.example.ecom.security.CustomAccessDeniedHandler;

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

    @Autowired
    private CustomAuthEntryPoint customAuthEntryPoint;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

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
     * - "/h2-console/**", "/api/auth/login", and "/api/auth/register" are public.
     * - "/api/products/**" GET requests are public (product browsing).
     * - "/api/products" POST requests require SELLER or ADMIN roles.
     * - "/api/auth/admin" requires the ADMIN role.
     * - All other "/api/**" endpoints require authentication.
     * - Any other requests also require authentication.
     * - Registers the custom UserDetailsService for user lookup.
     * - Adds the JWT authentication filter before the standard username/password
     * filter.
     * - Configures custom authentication entry point and access denied handler.
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
                        // Public product GET endpoints
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/products/**").permitAll()
                        // Only SELLER or ADMIN can POST to /api/products (add product)
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/products")
                        .hasAnyRole("SELLER", "ADMIN")
                        // Admin-only endpoint
                        .requestMatchers("/api/auth/admin").hasRole("ADMIN")
                        // All other API endpoints require authentication
                        .requestMatchers("/api/**").authenticated()
                        // Any other requests require authentication
                        .anyRequest().authenticated())
                .userDetailsService(userDetailsService)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler));
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