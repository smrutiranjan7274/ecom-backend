package com.example.ecom.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private final JwtUtil jwtUtil = new JwtUtil();

    @Test
    void generateAndValidateToken() {
        // Generate a token for a user with a specific role.
        // This simulates a user logging in and receiving a JWT.
        String token = jwtUtil.generateToken("user1", "ADMIN");

        // Assert that the token is valid.
        // This checks if the token's signature is valid and if it hasn't expired.
        assertThat(jwtUtil.validateToken(token)).isTrue();

        // Assert that the username extracted from the token is correct.
        // This verifies that the token contains the correct user information.
        assertThat(jwtUtil.getUsernameFromToken(token)).isEqualTo("user1");

        // Assert that the role extracted from the token is correct.
        // This verifies that the token contains the correct user role.
        assertThat(jwtUtil.getRoleFromToken(token)).isEqualTo("ADMIN");
    }

    @Test
    void validateToken_invalid() {
        // Assert that an invalid token is not validated.
        // This tests the scenario where a token is malformed or has an invalid signature.
        assertThat(jwtUtil.validateToken("invalid.token.here")).isFalse();
    }
}
