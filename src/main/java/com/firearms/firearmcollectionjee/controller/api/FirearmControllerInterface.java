package com.firearms.firearmcollectionjee.controller.api;

import com.firearms.firearmcollectionjee.dto.firearm.*;
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
    @Path("/weaponfamilies/firearms/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    void putFirearm(@PathParam("id") UUID id, PutFirearmRequest firearm);
    @PUT
    @Path("/weaponfamilies/{weaponFamilyId}/firearms/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    void putFirearmWithWeaponFamilyId(@PathParam("id") UUID id, @PathParam("weaponFamilyId") UUID weaponFamilyId, PutFirearmWithNoWeaponFamilyRequest firearm);
    @PATCH
    @Path("/firearms/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void patchFirearm(@PathParam("id") UUID id, PatchFirearmRequest firearm);
    @DELETE
    @Path("/firearms/{id}")
    void deleteFirearm(@PathParam("id") UUID id);
}
