package com.firearms.firearmcollectionjee.dto.user.function;

import com.firearms.firearmcollectionjee.dto.user.UserCreateRequestDto;
import com.firearms.firearmcollectionjee.model.User;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Function;

/**
 * Builds a new {@link User} from a {@link UserCreateRequestDto}.
 */
public class RequestToUserFunction implements Function<UserCreateRequestDto, User> {
    @Override
    public User apply(UserCreateRequestDto dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setLogin(dto.getLogin());
        user.setEmail(dto.getEmail());
        user.setBirthDate(dto.getBirthDate());
        user.setRoles(dto.getRoles() == null ? new ArrayList<>() : new ArrayList<>(dto.getRoles()));
        return user;
    }
}
