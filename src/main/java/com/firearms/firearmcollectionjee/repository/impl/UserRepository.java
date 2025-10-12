package com.firearms.firearmcollectionjee.repository.impl;

import com.firearms.firearmcollectionjee.model.User;
import com.firearms.firearmcollectionjee.repository.api.UserRepositoryInterface;
import com.firearms.firearmcollectionjee.storage.DataStorage;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of UserRepositoryInterface.
 * Handles all CRUD operations for users using the in-memory data storage.
 */
public class UserRepository implements UserRepositoryInterface {
    
    private final DataStorage dataStorage;
    
    public UserRepository(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }
    
    @Override
    public User save(User user) {
        final User userToSave;
        if (user.getId() == null) {
            User newUser = new User();
            newUser.setId(UUID.randomUUID());
            newUser.setLogin(user.getLogin());
            newUser.setBirthDate(user.getBirthDate());
            newUser.setEmail(user.getEmail());
            newUser.setAvatarPath(user.getAvatarPath());
            newUser.setRoles(user.getRoles());
            userToSave = newUser;
        } else {
            userToSave = user;
        }
        
        // Remove existing user with same ID if present
        dataStorage.getUsers().removeIf(existingUser -> existingUser.getId().equals(userToSave.getId()));
        
        // Add the user
        dataStorage.getUsers().add(userToSave);
        
        return userToSave;
    }
    
    @Override
    public Optional<User> findById(UUID id) {
        return dataStorage.getUsers().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }
    
    @Override
    public Optional<User> findByLogin(String login) {
        return dataStorage.getUsers().stream()
                .filter(user -> login.equals(user.getLogin()))
                .findFirst();
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return dataStorage.getUsers().stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst();
    }
    
    @Override
    public Set<User> findAll() {
        return dataStorage.getUsers().stream()
                .collect(Collectors.toSet());
    }
    
    @Override
    public boolean deleteById(UUID id) {
        return dataStorage.getUsers().removeIf(user -> user.getId().equals(id));
    }
    
    @Override
    public boolean delete(User user) {
        return dataStorage.getUsers().remove(user);
    }
    
    @Override
    public boolean existsById(UUID id) {
        return dataStorage.getUsers().stream()
                .anyMatch(user -> user.getId().equals(id));
    }
    
    @Override
    public boolean existsByLogin(String login) {
        return dataStorage.getUsers().stream()
                .anyMatch(user -> login.equals(user.getLogin()));
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return dataStorage.getUsers().stream()
                .anyMatch(user -> email.equals(user.getEmail()));
    }
    
    @Override
    public long count() {
        return dataStorage.getUsers().size();
    }
    
    @Override
    public void deleteAll() {
        dataStorage.getUsers().clear();
    }
}