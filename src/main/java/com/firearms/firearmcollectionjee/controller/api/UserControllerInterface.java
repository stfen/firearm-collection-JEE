package com.firearms.firearmcollectionjee.controller.api;

import com.firearms.firearmcollectionjee.dto.user.GetUsersResponse;
import com.firearms.firearmcollectionjee.dto.user.PatchUserRequest;
import com.firearms.firearmcollectionjee.dto.user.PutUserRequest;
import com.firearms.firearmcollectionjee.model.User;
import com.firearms.firearmcollectionjee.dto.user.GetUserResponse;

import java.util.UUID;

/**
 * Controller interface defining user-related operations exposed to the presentation layer.
 * This is a thin abstraction over the service layer to decouple web / UI layer from business logic.
 */
public interface UserControllerInterface {

    void putUser(UUID id, PutUserRequest request);

    void patchUser(UUID id, PatchUserRequest request);

    GetUserResponse getUserById(UUID id);

    GetUserResponse getUserByLogin(String login);

    GetUsersResponse getAllUsers();

    void deleteUser(UUID id);

    boolean isLoginAvailable(String login);

    boolean isEmailAvailable(String email);

    /* Avatar (image) operations */
    byte[] getUserAvatar(UUID id);

    void putUserAvatar(UUID id, java.io.InputStream avatarStream);

    void deleteUserAvatar(UUID id);
}
