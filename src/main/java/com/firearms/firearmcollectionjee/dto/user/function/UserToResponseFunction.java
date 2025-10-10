package com.firearms.firearmcollectionjee.dto.user.function;

import com.firearms.firearmcollectionjee.dto.user.GetUserResponse;
import com.firearms.firearmcollectionjee.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Converts a domain {@link User} into a {@link GetUserResponse} DTO.
 */
public class UserToResponseFunction implements Function<User, GetUserResponse> {
    @Override
    public GetUserResponse apply(User user) {
        if (user == null) {
            return null;
        }
        return new GetUserResponse(
                user.getId(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthDate(),
                user.getRoles() == null ? List.of() : new ArrayList<>(user.getRoles())
        );
    }
}
