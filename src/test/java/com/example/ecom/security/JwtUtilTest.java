package com.example.ecom.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        String token = jwtUtil.generateToken("user1", "ADMIN");

        assertThat(jwtUtil.validateToken(token)).isTrue();
        assertThat(jwtUtil.getUsernameFromToken(token)).isEqualTo("user1");
        assertThat(jwtUtil.getRoleFromToken(token)).isEqualTo("ADMIN");
    }

    @Test
    void validateToken_invalid() {
        assertThat(jwtUtil.validateToken("invalid.token.here")).isFalse();
    }

    @Test
    void validateToken_expired() {
        // Generate an expired token (expiration in the past)
        String expiredToken = jwtUtil.generateTokenWithCustomExpiration("user1", "ADMIN", -1000L);
        assertThat(jwtUtil.validateToken(expiredToken)).isFalse();
    }

    @Test
    void validateToken_nullOrEmpty() {
        assertThat(jwtUtil.validateToken(null)).isFalse();
        assertThat(jwtUtil.validateToken("")).isFalse();
    }

    @Test
    void generateToken_differentRoles() {
        String userToken = jwtUtil.generateToken("user2", "USER");
        assertThat(jwtUtil.getRoleFromToken(userToken)).isEqualTo("USER");

        String managerToken = jwtUtil.generateToken("user3", "MANAGER");
        assertThat(jwtUtil.getRoleFromToken(managerToken)).isEqualTo("MANAGER");
    }

    @Test
    void validateToken_malformedToken() {
        assertThat(jwtUtil.validateToken("not.even.close")).isFalse();
        assertThat(jwtUtil.validateToken("eyJhbGciOiJIUzI1NiJ9.only.two.parts")).isFalse();
    }
}
