package com.firearms.firearmcollectionjee.controller.api;

import com.firearms.firearmcollectionjee.dto.weaponfamily.GetWeaponFamiliesResponse;
import com.firearms.firearmcollectionjee.dto.weaponfamily.GetWeaponFamilyResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.UUID;

@Path("")
public interface WeaponFamilyControllerInterface {
    @GET
    @Path("/weaponfamilies")
    @Produces(MediaType.APPLICATION_JSON)
    GetWeaponFamiliesResponse getWeaponFamilies();
    @GET
    @Path("/weaponfamilies/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    GetWeaponFamilyResponse getWeaponFamily(@PathParam("id") UUID id);
    @DELETE
    @Path("/weaponfamilies/{id}")
    void deleteWeaponFamily(@PathParam("id") UUID id);
}
