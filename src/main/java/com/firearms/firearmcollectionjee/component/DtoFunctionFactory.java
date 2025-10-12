package com.firearms.firearmcollectionjee.component;

import com.firearms.firearmcollectionjee.dto.user.function.RequestToUserFunction;
import com.firearms.firearmcollectionjee.dto.user.function.UpdateUserWithRequestFunction;
import com.firearms.firearmcollectionjee.dto.user.function.UserToResponseFunction;
import com.firearms.firearmcollectionjee.dto.user.function.UsersToResponseFunction;

public class DtoFunctionFactory {

    public RequestToUserFunction requestToUserFunction() {
        return new RequestToUserFunction();
    }

    public UpdateUserWithRequestFunction updateUserWithRequestFunction() {
        return new UpdateUserWithRequestFunction();
    }

    public UserToResponseFunction userToResponseFunction() {
        return new UserToResponseFunction();
    }

    public UsersToResponseFunction usersToResponseFunction() {
        return new UsersToResponseFunction();
    }
}
