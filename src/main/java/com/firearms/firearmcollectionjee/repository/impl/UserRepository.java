package com.firearms.firearmcollectionjee.repository.impl;

import com.firearms.firearmcollectionjee.model.User;
import com.firearms.firearmcollectionjee.repository.api.UserRepositoryInterface;
import com.firearms.firearmcollectionjee.storage.DataStorage;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of UserRepositoryInterface.
 * Handles all CRUD operations for users using the in-memory data storage.
 */
@RequestScoped
@NoArgsConstructor(force = true)
public class UserRepository implements UserRepositoryInterface {
    
    private final DataStorage dataStorage;

    @Inject
    public UserRepository(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }
    
    @Override
    public void create(User user) {
        dataStorage.createUser(user);
    }

    @Override
    public void update(User user) {
        dataStorage.updateUser(user);
    }

    @Override
    public void delete(User entity) {
        dataStorage.deleteUser(entity);
    }


    @Override
    public Optional<User> findById(UUID id) {
        return dataStorage.findAllUsers().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }
    
    @Override
    public Optional<User> findByLogin(String login) {
        return dataStorage.findAllUsers().stream()
                .filter(user -> login.equals(user.getLogin()))
                .findFirst();
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return dataStorage.findAllUsers().stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst();
    }
    
    @Override
    public List<User> findAll() {
        return dataStorage.findAllUsers();
    }
    
    @Override
    public boolean existsById(UUID id) {
        return dataStorage.findAllUsers().stream()
                .anyMatch(user -> user.getId().equals(id));
    }
    
    @Override
    public boolean existsByLogin(String login) {
        return dataStorage.findAllUsers().stream()
                .anyMatch(user -> login.equals(user.getLogin()));
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return dataStorage.findAllUsers().stream()
                .anyMatch(user -> email.equals(user.getEmail()));
    }
}