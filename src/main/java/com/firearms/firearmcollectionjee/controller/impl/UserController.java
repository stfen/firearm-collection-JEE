package com.firearms.firearmcollectionjee.controller.impl;

import com.firearms.firearmcollectionjee.controller.api.UserControllerInterface;
import com.firearms.firearmcollectionjee.model.User;
import com.firearms.firearmcollectionjee.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Simple implementation of {@link UserControllerInterface} which delegates calls to {@link UserService}.
 * This class contains no framework annotations so it can be used in plain Java or wired manually.
 */
public class UserController implements UserControllerInterface {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User createUser(User user) {
        return userService.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userService.updateUser(user);
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        return userService.findById(id);
    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        return userService.findByLogin(login);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userService.findByEmail(email);
    }

    @Override
    public Set<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    public boolean deleteUser(UUID id) {
        return userService.deleteUser(id);
    }

    @Override
    public boolean deleteUserByLogin(String login) {
        return userService.deleteUserByLogin(login);
    }

    @Override
    public List<User> getUsersByRole(String role) {
        return userService.getUsersByRole(role);
    }

    @Override
    public boolean isLoginAvailable(String login) {
        return userService.isLoginAvailable(login);
    }

    @Override
    public boolean isEmailAvailable(String email) {
        return userService.isEmailAvailable(email);
    }

    @Override
    public long getUserCount() {
        return userService.getUserCount();
    }
}
