package com.firearms.firearmcollectionjee.dto.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DTO wrapping a collection of users for list responses.
 */
public class GetUsersResponse {

    private List<GetUserResponse> users = new ArrayList<>();

    public GetUsersResponse() {
    }

    public GetUsersResponse(List<GetUserResponse> users) {
        if (users != null) {
            this.users = new ArrayList<>(users);
        }
    }

    public List<GetUserResponse> getUsers() {
        return Collections.unmodifiableList(users);
    }

    public void setUsers(List<GetUserResponse> users) {
        this.users = users == null ? new ArrayList<>() : new ArrayList<>(users);
    }
}
