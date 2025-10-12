package com.firearms.firearmcollectionjee.service;

import com.firearms.firearmcollectionjee.model.User;
import com.firearms.firearmcollectionjee.repository.api.UserRepositoryInterface;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for User business logic.
 * Handles business operations and validation for users.
 */
public class UserService {
    
    private final UserRepositoryInterface userRepository;
    private final String avatarBasePath; // default, can be overridden via context-param
    
    public UserService(UserRepositoryInterface userRepository, String avatarBasePath) {
        this.userRepository = userRepository;
        this.avatarBasePath = avatarBasePath;
    }
    
    /**
     * Create a new user with validation.
     * @param user the user to create
     * @return the created user
     * @throws IllegalArgumentException if validation fails
     */
    public void createUser(User user) {
        validateUser(user);

        if (userRepository.existsByLogin(user.getLogin())) {
            throw new IllegalArgumentException("User with login '" + user.getLogin() + "' already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User with email '" + user.getEmail() + "' already exists");
        }
        
        userRepository.create(user);
    }
    
    /**
     * Update an existing user.
     * @param user the user to update
     * @return the updated user
     * @throws IllegalArgumentException if validation fails or user not found
     */
    public void updateUser(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("User ID cannot be null for update operation");
        }
        
        if (!userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("User with ID '" + user.getId() + "' not found");
        }
        
        validateUser(user);

        Optional<User> existingUserWithLogin = userRepository.findByLogin(user.getLogin());
        if (existingUserWithLogin.isPresent() && !existingUserWithLogin.get().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Login '" + user.getLogin() + "' is already taken by another user");
        }

        Optional<User> existingUserWithEmail = userRepository.findByEmail(user.getEmail());
        if (existingUserWithEmail.isPresent() && !existingUserWithEmail.get().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Email '" + user.getEmail() + "' is already taken by another user");
        }
        
        userRepository.update(user);
    }
    
    /**
     * Find user by ID.
     * @param id the user ID
     * @return Optional containing the user if found
     */
    public Optional<User> findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return userRepository.findById(id);
    }
    
    /**
     * Find user by login.
     * @param login the user login
     * @return Optional containing the user if found
     */
    public Optional<User> findByLogin(String login) {
        if (login == null || login.trim().isEmpty()) {
            throw new IllegalArgumentException("Login cannot be null or empty");
        }
        return userRepository.findByLogin(login.trim());
    }
    
    /**
     * Get all users.
     * @return Set of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    
    /**
     * Delete user by ID.
     * @param id the user ID
     */
    public void deleteUser(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        userRepository.findById(id).ifPresent(userRepository::delete);
    }
    
    /**
     * Check if user exists by ID.
     * @param id the user ID
     * @return true if user exists
     */
    public boolean userExists(UUID id) {
        if (id == null) {
            return false;
        }
        return userRepository.existsById(id);
    }
    
    /**
     * Check if login is available.
     * @param login the login to check
     * @return true if login is available (not taken)
     */
    public boolean isLoginAvailable(String login) {
        if (login == null || login.trim().isEmpty()) {
            return false;
        }
        return !userRepository.existsByLogin(login.trim());
    }
    
    /**
     * Check if email is available.
     * @param email the email to check
     * @return true if email is available (not taken)
     */
    public boolean isEmailAvailable(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return !userRepository.existsByEmail(email.trim().toLowerCase());
    }

    /**
     * Store or replace a user's avatar image.
     * @param id user id
     * @param avatar input stream with image bytes (PNG expected currently)
     */
    public void updateAvatar(UUID id, InputStream avatar) {
        if (id == null || avatar == null) {
            throw new IllegalArgumentException("User id and avatar stream must not be null");
        }
        userRepository.findById(id).ifPresent(user -> {
            try {
                String fileName = user.getLogin() + "_" + UUID.randomUUID() + ".png"; // simple naming
                Path target = Paths.get(avatarBasePath, fileName);
                Files.createDirectories(target.getParent());
                Files.write(target, avatar.readAllBytes());
                user.setAvatarPath(target.toString());
                userRepository.update(user);
            } catch (IOException e) {
                throw new IllegalStateException("Error saving avatar for user ID: " + id, e);
            }
        });
    }

    /**
     * Retrieve avatar bytes for a user. Returns empty array if none.
     */
    public byte[] getAvatar(UUID id) {
        if (id == null) {
            return new byte[0];
        }
        return userRepository.findById(id).map(user -> {
            String pathStr = user.getAvatarPath();
            if (pathStr == null || pathStr.isBlank()) {
                return new byte[0];
            }
            Path p = Paths.get(pathStr);
            if (!Files.exists(p)) {
                return new byte[0];
            }
            try {
                return Files.readAllBytes(p);
            } catch (IOException e) {
                throw new IllegalStateException("Error reading avatar for user ID: " + id, e);
            }
        }).orElse(new byte[0]);
    }

    /**
     * Delete avatar file (if exists) and clear stored path.
     */
    public void deleteAvatar(UUID id) {
        if (id == null) {
            return;
        }
        userRepository.findById(id).ifPresent(user -> {
            String pathStr = user.getAvatarPath();
            if (pathStr == null || pathStr.isBlank()) {
                return;
            }
            Path p = Paths.get(pathStr);
            try {
                Files.deleteIfExists(p);
            } catch (IOException e) {
                throw new IllegalStateException("Error deleting avatar for user ID: " + id, e);
            }
            user.setAvatarPath("");
            userRepository.update(user);
        });
    }
    
    /**
     * Validate user data.
     * @param user the user to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        
        if (user.getLogin() == null || user.getLogin().trim().isEmpty()) {
            throw new IllegalArgumentException("User login cannot be null or empty");
        }
        
        if (user.getLogin().trim().length() < 3) {
            throw new IllegalArgumentException("User login must be at least 3 characters long");
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("User email cannot be null or empty");
        }
        
        if (!isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        if (user.getBirthDate() != null && user.getBirthDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Birth date cannot be in the future");
        }
    }
    
    /**
     * Simple email validation.
     * @param email the email to validate
     * @return true if email format is valid
     */
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
}