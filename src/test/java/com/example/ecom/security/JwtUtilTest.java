package com.example.ecom.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private static final String TEST_SECRET_KEY = "testSecretKeyThatIsLongEnoughForHS256Algorithm";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        jwtUtil.setJwtSecret(TEST_SECRET_KEY);
    }

    @Test
    void generateAndValidateToken() {
        // Generate a token for a user with a specific role.
        // This simulates a user logging in and receiving a JWT.
        // The JwtUtil should be configured to load the secret key from application.properties.
        String token = jwtUtil.generateToken("user1", "ADMIN");

        // Assert that the token is valid.
        assertThat(jwtUtil.validateToken(token)).isTrue();

        // Assert that the username extracted from the token is correct.
        assertThat(jwtUtil.getUsernameFromToken(token)).isEqualTo("user1");

        // Assert that the role extracted from the token is correct.
        assertThat(jwtUtil.getRoleFromToken(token)).isEqualTo("ADMIN");
    }

    @Test
    void validateToken_invalid() {
        // Assert that an invalid token is not validated.
        // This tests the scenario where a token is malformed or has an invalid signature.
        assertThat(jwtUtil.validateToken("invalid.token.here")).isFalse();
    }

    @Test
    void validateToken_expired() {
        // Generate an expired token
        String expiredToken = jwtUtil.generateTokenWithCustomExpiration("user1", "ADMIN", -1000L);
        
        // Assert that expired token is invalid
        assertThat(jwtUtil.validateToken(expiredToken)).isFalse();
    }

    @Test
    void validateToken_nullOrEmpty() {
        // Test null token
        assertThat(jwtUtil.validateToken(null)).isFalse();
        
        // Test empty token
        assertThat(jwtUtil.validateToken("")).isFalse();
    }


    @Test
    void generateToken_differentRoles() {
        // Test USER role
        String userToken = jwtUtil.generateToken("user2", "USER");
        assertThat(jwtUtil.getRoleFromToken(userToken)).isEqualTo("USER");
        
        // Test MANAGER role
        String managerToken = jwtUtil.generateToken("user3", "MANAGER");
        assertThat(jwtUtil.getRoleFromToken(managerToken)).isEqualTo("MANAGER");
    }

    @Test
    void validateToken_malformedToken() {
        // Test completely malformed token
        assertThat(jwtUtil.validateToken("not.even.close")).isFalse();
        
        // Test partially malformed token
        assertThat(jwtUtil.validateToken("eyJhbGciOiJIUzI1NiJ9.only.two.parts")).isFalse();
    }
}
