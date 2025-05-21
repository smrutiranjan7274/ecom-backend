package com.example.ecom.repository;

import com.example.ecom.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataMongoTest
@TestPropertySource(properties = "spring.data.mongodb.uri=mongodb://localhost:27017/test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveUser() {
        User user = new User();
        user.setEmail("test@example.com");
        User saved = userRepository.save(user);
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void testFindById() {
        User user = new User();
        user.setEmail("test2@example.com");
        User saved = userRepository.save(user);
        Optional<User> found = userRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test2@example.com");
    }

    @Test
    void testFindByEmail() {
        User user = new User();
        user.setEmail("unique@example.com");
        userRepository.save(user);
        Optional<User> found = userRepository.findByEmail("unique@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("unique@example.com");
    }
}
