package com.firearms.firearmcollectionjee.dto.user.function;

import com.firearms.firearmcollectionjee.dto.user.GetUserResponse;
import com.firearms.firearmcollectionjee.dto.user.GetUsersResponse;
import com.firearms.firearmcollectionjee.model.User;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Converts a list of {@link User} to a {@link GetUsersResponse} wrapper DTO.
 */
public class UsersToResponseFunction implements Function<List<User>, GetUsersResponse> {
    private final UserToResponseFunction singleMapper = new UserToResponseFunction();

    @Override
    public GetUsersResponse apply(List<User> users) {
        if (users == null) {
            return new GetUsersResponse(List.of());
        }
        return new GetUsersResponse(
                users.stream()
                        .filter(Objects::nonNull)
                        .map(singleMapper)
                        .collect(Collectors.toList())
        );
    }
}
