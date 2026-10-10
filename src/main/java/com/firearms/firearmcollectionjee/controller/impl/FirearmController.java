package com.firearms.firearmcollectionjee.controller.impl;

import com.firearms.firearmcollectionjee.component.DtoFunctionFactory;
import com.firearms.firearmcollectionjee.controller.api.FirearmControllerInterface;
import com.firearms.firearmcollectionjee.dto.firearm.*;
import com.firearms.firearmcollectionjee.entity.enums.UserRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.transaction.TransactionalException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import com.firearms.firearmcollectionjee.service.FirearmService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.security.enterprise.SecurityContext;
import lombok.extern.java.Log;

import java.util.UUID;
import java.util.logging.Level;


@Path("")
@Log
@RolesAllowed(UserRoles.USER)
public class FirearmController implements FirearmControllerInterface {

    private FirearmService firearmService;
    private final DtoFunctionFactory factory;
    private final UriInfo uriInfo;
    private HttpServletResponse response;
    private SecurityContext securityContext;

    @Context
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    @Inject
    public FirearmController(
            DtoFunctionFactory factory,
            @SuppressWarnings("CdiInjectionPointsInspection") UriInfo uriInfo) {
        this.factory = factory;
        this.uriInfo = uriInfo;
    }

    @Inject
    public void setSecurityContext(@SuppressWarnings("CdiInjectionPointsInspection") SecurityContext securityContext) {
        this.securityContext = securityContext;
    }

    @EJB
    public void setService(FirearmService service) {
        this.firearmService = service;
    }


    @Override
    public GetFirearmsResponse getFirearms() {
        return factory.firearmsToResponseFunction().apply(firearmService.findAllForCallerPrincipal());
    }

    @Override
    public GetFirearmsResponse getWeaponFamilyFirearms(UUID weaponFamilyId) {
        return firearmService.findAllByWeaponFamily(weaponFamilyId)
                .map(factory.firearmsToResponseFunction())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public GetFirearmsResponse getUserFirearms(UUID userId) {
        return firearmService.findAllByUser(userId)
                .map(factory.firearmsToResponseFunction())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public GetFirearmResponse getFirearm(UUID id) {
        return firearmService.findById(id)
                .map(factory.firearmToResponseFunction())
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public void putFirearm(UUID id, PutFirearmRequest request) {
        try {
            FirearmService svc = this.firearmService;
            if (securityContext.isCallerInRole(com.firearms.firearmcollectionjee.entity.enums.UserRoles.ADMIN)) {
                svc.createFirearm(factory.requestToFirearmFunction().apply(id, request));
            } else {
                svc.createForCallerPrincipal(factory.requestToFirearmFunction().apply(id, request));
            }
            String location = uriInfo.getBaseUriBuilder()
                .path("api")
                .path("firearms")
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
    public void putFirearmWithWeaponFamilyId(UUID id, UUID weaponFamilyId, PutFirearmWithNoWeaponFamilyRequest request) {
        try {
        FirearmService svc = this.firearmService;
        if (securityContext.isCallerInRole(com.firearms.firearmcollectionjee.entity.enums.UserRoles.ADMIN)) {
        svc.createFirearm(
            factory.requestToFirearmWithNoWeaponFamilyFunction().apply(id, weaponFamilyId, request)
        );
        } else {
        svc.createForCallerPrincipal(
            factory.requestToFirearmWithNoWeaponFamilyFunction().apply(id, weaponFamilyId, request)
        );
        }
            String location = uriInfo.getBaseUriBuilder()
                    .path("api")
                    .path("firearms")
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
    public void patchFirearm(UUID id, PatchFirearmRequest request) {
        firearmService.findById(id).ifPresentOrElse(
                entity -> firearmService.updateFirearm(factory.updateFirearmWithRequestFunction().apply(entity, request)),
                () -> {
                    throw new NotFoundException();
                }
        );
    }

    @Override
    public void deleteFirearm(UUID id) {
        firearmService.findById(id).ifPresentOrElse(
                entity -> firearmService.deleteFirearm(id),
                () -> {
                    throw new NotFoundException();
                }
        );
    }
}
