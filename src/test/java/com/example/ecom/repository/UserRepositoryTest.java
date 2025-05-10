package com.example.ecom.repository;

import com.example.ecom.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveAndFindByUsername() {
        // Arrange: Set up the test data and save a user to the database.
        // Create a User object with a username, password, and role.
        User user = User.builder()
                .username("testuser")
                .password("pass")
                .role("USER")
                .build();
        // Save the user to the database using the UserRepository.
        userRepository.save(user);

        // Act: Retrieve the user from the database using the findByUsername method.
        Optional<User> found = userRepository.findByUsername("testuser");

        // Assert: Verify that the user was found and that the retrieved user has the correct role.
        // Assert that the Optional contains a User object.
        assertThat(found).isPresent();
        // Assert that the role of the retrieved User object is equal to "USER".
        assertThat(found.get().getRole()).isEqualTo("USER");
    }
}
