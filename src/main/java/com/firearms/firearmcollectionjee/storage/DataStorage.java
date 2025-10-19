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

    public synchronized List<User> findAllUsers() {
        return users.stream()
                .map(cloningUtility::clone)
                .collect(Collectors.toList());
    }

    public synchronized void createUser(User value) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (users.stream().anyMatch(u -> u.getId().equals(value.getId()))) {
            throw new IllegalArgumentException("The user id \"%s\" is not unique".formatted(value.getId()));
        }
        users.add(cloningUtility.clone(value));
    }

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

    public synchronized List<Firearm> findAllFirearms() {
        return firearms.stream()
                .map(cloningUtility::clone)
                .collect(Collectors.toList());
    }

    public synchronized void createFirearm(Firearm value) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException("Firearm cannot be null");
        }
        if (firearms.stream().anyMatch(f -> f.getId().equals(value.getId()))) {
            throw new IllegalArgumentException("The firearm id \"%s\" already exists".formatted(value.getId()));
        }
        firearms.add(cloningUtility.clone(value));
    }

    public synchronized void updateFirearm(Firearm value) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException("Firearm cannot be null");
        }
        boolean removed = firearms.removeIf(f -> f.getId().equals(value.getId()));
        if (!removed) {
            throw new IllegalArgumentException("The firearm id \"%s\" does not exist".formatted(value.getId()));
        }
        firearms.add(cloningUtility.clone(value));
    }

    public synchronized void deleteFirearm(Firearm value) throws IllegalArgumentException {
        boolean isRemoved = firearms.removeIf(f -> f.getId().equals(value.getId()));
        if (!isRemoved) {
            throw new IllegalArgumentException("The firearm id \"%s\" does not exist".formatted(value.getId()));
        }
    }
}