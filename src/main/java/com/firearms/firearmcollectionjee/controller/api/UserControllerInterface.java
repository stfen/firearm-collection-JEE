package com.firearms.firearmcollectionjee.controller.api;

import com.firearms.firearmcollectionjee.dto.user.GetUsersResponse;
import com.firearms.firearmcollectionjee.dto.user.PatchUserRequest;
import com.firearms.firearmcollectionjee.dto.user.PutUserRequest;
import com.firearms.firearmcollectionjee.dto.user.GetUserResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.UUID;

/**
 * JAX-RS annotated controller interface exposing user-related REST endpoints.
 */
@Path("")
public interface UserControllerInterface {

    @PUT
    @Path("/users/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void putUser(@PathParam("id") UUID id, PutUserRequest request);

    @PATCH
    @Path("/users/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void patchUser(@PathParam("id") UUID id, PatchUserRequest request);

    @GET
    @Path("/users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    GetUserResponse getUserById(@PathParam("id") UUID id);

    @GET
    @Path("/users/login/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    GetUserResponse getUserByLogin(@PathParam("login") String login);

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    GetUsersResponse getAllUsers();

    @DELETE
    @Path("/users/{id}")
    void deleteUser(@PathParam("id") UUID id);

    /* Avatar (image) operations */
    @GET
    @Path("/users/{id}/avatar")
    @Produces("image/png")
    byte[] getUserAvatar(@PathParam("id") UUID id);

    @PUT
    @Path("/users/{id}/avatar")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    void putUserAvatar(@PathParam("id") UUID id, @jakarta.ws.rs.core.Context jakarta.servlet.http.HttpServletRequest request);

    @DELETE
    @Path("/users/{id}/avatar")
    void deleteUserAvatar(@PathParam("id") UUID id);
}
