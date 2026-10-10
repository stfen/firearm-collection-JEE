package com.firearms.firearmcollectionjee.repository.api;

import com.firearms.firearmcollectionjee.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface for User repository operations.
 * Defines the contract for all CRUD operations on User entities.
 */
public interface UserRepositoryInterface {
    
    /**
     * Save a new user or update an existing one.
     * @param user the user to save
     * @return the saved user
     */
    void create(User user);
    void update(User user);
    void delete(User user);
    /**
     * Find user by ID.
     * @param id the user ID
     * @return Optional containing the user if found
     */
    Optional<User> findById(UUID id);
    
    /**
     * Find user by login.
     * @param login the user login
     * @return Optional containing the user if found
     */
    Optional<User> findByLogin(String login);
    
    /**
     * Find user by email.
     * @param email the user email
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find all users.
     * @return Set of all users
     */
    List<User> findAll();
    
    /**
     * Check if user exists by ID.
     * @param id the user ID
     * @return true if user exists
     */
    boolean existsById(UUID id);
    
    /**
     * Check if user exists by login.
     * @param login the user login
     * @return true if user exists
     */
    boolean existsByLogin(String login);
    
    /**
     * Check if user exists by email.
     * @param email the user email
     * @return true if user exists
     */
    boolean existsByEmail(String email);
}