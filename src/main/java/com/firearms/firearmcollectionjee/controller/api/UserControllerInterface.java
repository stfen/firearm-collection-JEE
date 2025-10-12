package com.firearms.firearmcollectionjee.controller.api;

import com.firearms.firearmcollectionjee.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Controller interface defining user-related operations exposed to the presentation layer.
 * This is a thin abstraction over the service layer to decouple web / UI layer from business logic.
 */
public interface UserControllerInterface {

    User createUser(User user);

    User updateUser(User user);

    Optional<User> getUserById(UUID id);

    Optional<User> getUserByLogin(String login);

    Optional<User> getUserByEmail(String email);

    Set<User> getAllUsers();

    boolean deleteUser(UUID id);

    boolean deleteUserByLogin(String login);

    List<User> getUsersByRole(String role);

    boolean isLoginAvailable(String login);

    boolean isEmailAvailable(String email);

    long getUserCount();

    /* Avatar (image) operations */
    byte[] getUserAvatar(UUID id);

    void putUserAvatar(UUID id, java.io.InputStream avatarStream);

    void deleteUserAvatar(UUID id);
}
