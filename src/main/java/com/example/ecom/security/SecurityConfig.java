package com.example.ecom.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http.headers(headers -> headers
                                .frameOptions(frame -> frame.disable())) // Allow H2 console access
                                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for APIs
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/api/**") // Allow access to API endpoints
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .sessionManagement(session -> session
                                                // Use stateless session management
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                // Add JWT filter here if needed

                return http.build();
        }
}