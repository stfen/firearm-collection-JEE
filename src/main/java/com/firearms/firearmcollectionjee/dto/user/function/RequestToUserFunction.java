package com.firearms.firearmcollectionjee.dto.user.function;

import com.firearms.firearmcollectionjee.dto.user.PutUserRequest;
import com.firearms.firearmcollectionjee.entity.User;

import java.util.UUID;
import java.util.function.BiFunction;

public class RequestToUserFunction implements BiFunction<UUID, PutUserRequest, User> {
    @Override
    public User apply(UUID uuid, PutUserRequest putUserRequest) {
        return User.builder()
                .id(uuid)
                .login(putUserRequest.getLogin())
                .email(putUserRequest.getEmail())
                .avatarPath("")
                .build();
    }
}
