package com.firearms.firearmcollectionjee.dto.user.function;

import com.firearms.firearmcollectionjee.dto.user.UserUpdateRequestDto;
import com.firearms.firearmcollectionjee.model.User;

import java.util.ArrayList;
import java.util.function.BiFunction;

/**
 * Updates an existing {@link User} with data from {@link UserUpdateRequestDto} (null fields skipped).
 */
public class UpdateUserWithRequestFunction implements BiFunction<UserUpdateRequestDto, User, User> {
    @Override
    public User apply(UserUpdateRequestDto dto, User existing) {
        if (dto == null || existing == null) {
            return existing;
        }
        if (dto.getLogin() != null) {
            existing.setLogin(dto.getLogin());
        }
        if (dto.getEmail() != null) {
            existing.setEmail(dto.getEmail());
        }
        if (dto.getBirthDate() != null) {
            existing.setBirthDate(dto.getBirthDate());
        }
        if (dto.getRoles() != null) {
            existing.setRoles(new ArrayList<>(dto.getRoles()));
        }
        return existing;
    }
}
