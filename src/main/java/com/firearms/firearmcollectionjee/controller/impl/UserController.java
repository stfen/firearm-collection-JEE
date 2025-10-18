package com.firearms.firearmcollectionjee.controller.impl;

import com.firearms.firearmcollectionjee.component.DtoFunctionFactory;
import com.firearms.firearmcollectionjee.controller.api.UserControllerInterface;
import com.firearms.firearmcollectionjee.controller.servlet.exception.BadRequestException;
import com.firearms.firearmcollectionjee.controller.servlet.exception.NotFoundException;
import com.firearms.firearmcollectionjee.dto.user.GetUserResponse;
import com.firearms.firearmcollectionjee.dto.user.GetUsersResponse;
import com.firearms.firearmcollectionjee.dto.user.PatchUserRequest;
import com.firearms.firearmcollectionjee.dto.user.PutUserRequest;
import com.firearms.firearmcollectionjee.model.User;
import com.firearms.firearmcollectionjee.service.UserService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import javax.swing.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Simple implementation of {@link UserControllerInterface} which delegates calls to {@link UserService}.
 * This class contains no framework annotations so it can be used in plain Java or wired manually.
 */
@RequestScoped
@NoArgsConstructor(force = true)
public class UserController implements UserControllerInterface {

    private final UserService userService;

    private final DtoFunctionFactory factory;

    @Inject
    public UserController(UserService userService, DtoFunctionFactory factory) {
        this.userService = userService;
        this.factory = factory;
    }

    @Override
    public void putUser(UUID id, PutUserRequest request) {
        try {
            userService.createUser(factory.requestToUserFunction().apply(id, request));
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException();
        }
    }

    @Override
    public void patchUser(UUID id, PatchUserRequest request) {
        userService.findById(id).ifPresentOrElse(
                entity -> userService.updateUser(factory.updateUserWithRequestFunction().apply(entity, request)),
                () -> {
                    throw new NotFoundException();
                }
        );
    }

    @Override
    public GetUserResponse getUserById(UUID id) {
        return userService.findById(id)
                .map(factory.userToResponseFunction())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public GetUserResponse getUserByLogin(String login) {
        return userService.findByLogin(login)
                .map(factory.userToResponseFunction())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public GetUsersResponse getAllUsers() {
        return factory.usersToResponseFunction().apply(userService.getAllUsers());
    }

    @Override
    public void deleteUser(UUID id) {
        userService.deleteUser(id);
    }


    @Override
    public byte[] getUserAvatar(UUID id) {
        return userService.findById(id)
                .map(u -> userService.getAvatar(id))
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public void putUserAvatar(UUID id, java.io.InputStream avatarStream) {
        userService.findById(id).ifPresentOrElse(
                u -> userService.updateAvatar(id, avatarStream),
                () -> { throw new NotFoundException(); }
        );
    }

    @Override
    public void deleteUserAvatar(UUID id) {
        userService.findById(id).ifPresentOrElse(
                u -> userService.deleteAvatar(id),
                () -> { throw new NotFoundException(); }
        );
    }
}
