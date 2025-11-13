package com.firearms.firearmcollectionjee.controller.impl;

import com.firearms.firearmcollectionjee.component.DtoFunctionFactory;
import com.firearms.firearmcollectionjee.controller.api.WeaponFamilyControllerInterface;
import com.firearms.firearmcollectionjee.dto.weaponfamily.GetWeaponFamiliesResponse;
import com.firearms.firearmcollectionjee.dto.weaponfamily.GetWeaponFamilyResponse;
import com.firearms.firearmcollectionjee.dto.weaponfamily.PutWeaponFamilyRequest;
import com.firearms.firearmcollectionjee.entity.WeaponFamily;
import com.firearms.firearmcollectionjee.entity.enums.UserRoles;
import com.firearms.firearmcollectionjee.service.WeaponFamilyService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.TransactionalException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.*;
import lombok.NoArgsConstructor;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.extern.java.Log;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;


@Path("")
@Log
public class WeaponFamilyController implements WeaponFamilyControllerInterface {

    private final WeaponFamilyService weaponFamilyService;
    private final DtoFunctionFactory factory;
    private final UriInfo uriInfo;
    private HttpServletResponse response;

    @Context
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    @Inject
    public WeaponFamilyController(WeaponFamilyService weaponFamilyService, DtoFunctionFactory factory, @SuppressWarnings("CdiInjectionPointsInspection") UriInfo uriInfo) {
        this.weaponFamilyService = weaponFamilyService;
        this.factory = factory;
        this.uriInfo = uriInfo;
    }

    @Override
    @RolesAllowed(UserRoles.USER)
    public GetWeaponFamiliesResponse getWeaponFamilies() {
        List<WeaponFamily> weaponFamilies = weaponFamilyService.getAllWeaponFamilies();
        return factory.weaponFamiliesToResponseFunction().apply(weaponFamilies);
    }

    @Override
    public GetWeaponFamilyResponse getWeaponFamily(UUID id) {
        return weaponFamilyService.findById(id)
                .map(factory.weaponFamilyToResponseFunction())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    @RolesAllowed(UserRoles.ADMIN)
    public void putWeaponFamily(UUID id, PutWeaponFamilyRequest request) {
        try {
            weaponFamilyService.createWeaponFamily(
                    factory.requestToWeaponFamilyFunction().apply(id, request)
            );
            String location = uriInfo.getBaseUriBuilder()
                    .path("api")
                    .path("weaponfamilies")
                    .path(id.toString())
                    .build()
                    .toString();
            response.setHeader("Location", location);
            throw new WebApplicationException(Response.status(Response.Status.CREATED).build());
        } catch (TransactionalException ex) {
            if (ex.getCause() instanceof IllegalArgumentException) {
                log.log(Level.WARNING, ex.getMessage(), ex);
                throw new BadRequestException(ex);
            }
            throw ex;
        }
    }

    @Override
    @jakarta.annotation.security.RolesAllowed(com.firearms.firearmcollectionjee.entity.enums.UserRoles.ADMIN)
    public void deleteWeaponFamily(UUID id) {
        weaponFamilyService.findById(id).ifPresentOrElse(
                entity -> weaponFamilyService.deleteWeaponFamily(id),
                () -> {
                    throw new NotFoundException();
                }
        );
    }
}
