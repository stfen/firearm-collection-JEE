package com.firearms.firearmcollectionjee.controller.api;

import com.firearms.firearmcollectionjee.dto.firearm.GetFirearmResponse;
import com.firearms.firearmcollectionjee.dto.firearm.GetFirearmsResponse;
import com.firearms.firearmcollectionjee.dto.firearm.PatchFirearmRequest;
import com.firearms.firearmcollectionjee.dto.firearm.PutFirearmRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.UUID;

@Path("")
public interface FirearmControllerInterface {
    @GET
    @Path("/firearms")
    @Produces(MediaType.APPLICATION_JSON)
    GetFirearmsResponse getFirearms();
    @GET
    @Path("/weaponfamilies/{id}/firearms")
    @Produces(MediaType.APPLICATION_JSON)
    GetFirearmsResponse getWeaponFamilyFirearms(@PathParam("id") UUID id);
    @GET
    @Path("/users/{id}/firearms")
    @Produces(MediaType.APPLICATION_JSON)
    GetFirearmsResponse getUserFirearms(@PathParam("id") UUID id);
    @GET
    @Path("/firearms/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    GetFirearmResponse getFirearm(@PathParam("id") UUID id);
    @PUT
    @Path("/firearms/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    void putFirearm(@PathParam("id") UUID id, PutFirearmRequest firearm);
    @PATCH
    @Path("/firearms/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void patchFirearm(@PathParam("id") UUID id, PatchFirearmRequest firearm);
    @DELETE
    @Path("/firearms/{id}")
    void deleteFirearm(@PathParam("id") UUID id);
}
