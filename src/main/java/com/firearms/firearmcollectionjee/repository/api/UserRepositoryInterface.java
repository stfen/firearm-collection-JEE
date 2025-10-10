package com.firearms.firearmcollectionjee.repository.api;

import com.firearms.firearmcollectionjee.model.User;

import java.util.Optional;
import java.util.Set;
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
    User save(User user);
    
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
    Set<User> findAll();
    
    /**
     * Delete user by ID.
     * @param id the user ID
     * @return true if user was deleted, false if not found
     */
    boolean deleteById(UUID id);
    
    /**
     * Delete user.
     * @param user the user to delete
     * @return true if user was deleted, false if not found
     */
    boolean delete(User user);
    
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
    
    /**
     * Count total number of users.
     * @return number of users
     */
    long count();
    
    /**
     * Delete all users.
     */
    void deleteAll();
}