package com.firearms.firearmcollectionjee.dto.user.function;

import com.firearms.firearmcollectionjee.dto.user.PatchUserRequest;
import com.firearms.firearmcollectionjee.entity.User;

import java.util.function.BiFunction;

public class UpdateUserWithRequestFunction implements BiFunction<User, PatchUserRequest, User> {

    @Override
    public User apply(User user, PatchUserRequest patchUserRequest) {
        return User.builder()
                .id(user.getId())
                .login(user.getLogin())
                .email(patchUserRequest.getEmail())
                .build();
    }
}
