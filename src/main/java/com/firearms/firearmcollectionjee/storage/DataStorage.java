package com.firearms.firearmcollectionjee.storage;

import com.firearms.firearmcollectionjee.model.Firearm;
import com.firearms.firearmcollectionjee.model.User;
import com.firearms.firearmcollectionjee.model.WeaponFamily;
import com.firearms.firearmcollectionjee.serialization.component.CloningUtility;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Simple in-memory database using HashSets for storing application data.
 * Repository classes will handle all CRUD operations.
 */
@Log
@ApplicationScoped
@NoArgsConstructor(force = true)
public class DataStorage {
    
    private final Set<Firearm> firearms = new HashSet<>();
    private final Set<WeaponFamily> weaponFamilies = new HashSet<>();
    private final Set<User> users = new HashSet<>();
    private final CloningUtility cloningUtility;

    @Inject
    public DataStorage(CloningUtility cloningUtility) {
        this.cloningUtility = cloningUtility;
    }
    
    public Set<Firearm> getFirearms() {
        return firearms;
    }
    
    public Set<WeaponFamily> getWeaponFamilies() {
        return weaponFamilies;
    }
    
    public Set<User> getUsers() {
        return users;
    }

    /**
     * Returns cloned list of all users to avoid exposing internal set references.
     * @return list (possibly empty) of all stored users
     */
    public synchronized List<User> findAllUsers() {
        return users.stream()
                .map(cloningUtility::clone)
                .collect(Collectors.toList());
    }

    /**
     * Stores a new user (clone) if its id is unique.
     * @param value user to store
     * @throws IllegalArgumentException if a user with the provided id already exists
     */
    public synchronized void createUser(User value) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (users.stream().anyMatch(u -> u.getId().equals(value.getId()))) {
            throw new IllegalArgumentException("The user id \"%s\" is not unique".formatted(value.getId()));
        }
        users.add(cloningUtility.clone(value));
    }

    /**
     * Updates an existing user by id, replacing stored instance with a clone of the provided value.
     * @param value user to update
     * @throws IllegalArgumentException if the user does not already exist
     */
    public synchronized void updateUser(User value) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        boolean removed = users.removeIf(u -> u.getId().equals(value.getId()));
        if (!removed) {
            throw new IllegalArgumentException("The user with id \"%s\" does not exist".formatted(value.getId()));
        }
        users.add(cloningUtility.clone(value));
    }

    public synchronized void deleteUser(User value) throws IllegalArgumentException {
        boolean isRemoved = users.removeIf(user -> user.getId().equals(value.getId()));
        if (!isRemoved) {
            throw new IllegalArgumentException("The user with id \"%s\" does not exist".formatted(value.getId()));
        }
    }
}