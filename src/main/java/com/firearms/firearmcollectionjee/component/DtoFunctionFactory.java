package com.firearms.firearmcollectionjee.component;

import com.firearms.firearmcollectionjee.dto.user.function.RequestToUserFunction;
import com.firearms.firearmcollectionjee.dto.user.function.UpdateUserWithRequestFunction;
import com.firearms.firearmcollectionjee.dto.user.function.UserToResponseFunction;
import com.firearms.firearmcollectionjee.dto.user.function.UsersToResponseFunction;

/**
 * Factory creating function objects for mapping between domain and DTO representations.
 * Mirrors the style of the provided sample, focusing on User mappings for now.
 */
public class DtoFunctionFactory {

    public UserToResponseFunction userToResponse() {
        return new UserToResponseFunction();
    }

    public UsersToResponseFunction usersToResponse() {
        return new UsersToResponseFunction();
    }

    public RequestToUserFunction requestToUser() {
        return new RequestToUserFunction();
    }

    public UpdateUserWithRequestFunction updateUser() {
        return new UpdateUserWithRequestFunction();
    }
}
