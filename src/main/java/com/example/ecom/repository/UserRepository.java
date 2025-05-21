package com.example.ecom.repository;

import com.example.ecom.model.User;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Find a user by their username.
     *
     * @param username the username of the user
     * @return an Optional containing the user if found, or empty if not found
     */
    Optional<User> findByEmail(String email);

    /**
     * Retrieve all users from the database.
     *
     * @return a list of all users
     */
    @NonNull
    List<User> findAll();
}
