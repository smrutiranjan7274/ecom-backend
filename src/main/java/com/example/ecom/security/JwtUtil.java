package com.example.ecom.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Utility class for handling JWT operations.
 */
@Component
public class JwtUtil {

    // =======================
    // JWT Configuration
    // =======================
    @Value("${jwt.secret}")
    private String JWT_SECRET;
    private static final long JWT_EXPIRATION_MS = 86400000; // 1 day

    // =======================
    // Key Management
    // =======================
    /**
     * Returns the signing key for JWT.
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
    }

    // =======================
    // Token Generation
    // =======================
    /**
     * Generates a JWT token for the given username and role.
     */
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // =======================
    // Token Parsing
    // =======================
    /**
     * Extracts the username (subject) from the JWT token.
     */
    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Extracts the role from the JWT token.
     */
    public String getRoleFromToken(String token) {
        return (String) getClaims(token).get("role");
    }

    // =======================
    // Token Validation
    // =======================
    /**
     * Validates the JWT token.
     */
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Parses the JWT token and returns the claims.
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateTokenWithCustomExpiration(String username, String role, long expirationTime) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public void setJwtSecret(String testSecretKey) {
        this.JWT_SECRET = testSecretKey;
    }
}