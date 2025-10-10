package com.firearms.firearmcollectionjee.storage;

import com.firearms.firearmcollectionjee.model.Firearm;
import com.firearms.firearmcollectionjee.model.User;
import com.firearms.firearmcollectionjee.model.WeaponFamily;

import java.util.HashSet;
import java.util.Set;

/**
 * Simple in-memory database using HashSets for storing application data.
 * Repository classes will handle all CRUD operations.
 */
public class DataStorage {
    
    private final Set<Firearm> firearms = new HashSet<>();
    private final Set<WeaponFamily> weaponFamilies = new HashSet<>();
    private final Set<User> users = new HashSet<>();
    
    public Set<Firearm> getFirearms() {
        return firearms;
    }
    
    public Set<WeaponFamily> getWeaponFamilies() {
        return weaponFamilies;
    }
    
    public Set<User> getUsers() {
        return users;
    }
}