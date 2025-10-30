package com.firearms.firearmcollectionjee.controller.impl;

import com.firearms.firearmcollectionjee.component.DtoFunctionFactory;
import com.firearms.firearmcollectionjee.controller.api.FirearmControllerInterface;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import com.firearms.firearmcollectionjee.dto.firearm.GetFirearmResponse;
import com.firearms.firearmcollectionjee.dto.firearm.GetFirearmsResponse;
import com.firearms.firearmcollectionjee.dto.firearm.PatchFirearmRequest;
import com.firearms.firearmcollectionjee.dto.firearm.PutFirearmRequest;
import com.firearms.firearmcollectionjee.entity.Firearm;
import com.firearms.firearmcollectionjee.service.FirearmService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@Path("")
public class FirearmController implements FirearmControllerInterface {

    private final FirearmService firearmService;
    private final DtoFunctionFactory factory;
    private final UriInfo uriInfo;
    private HttpServletResponse response;

    @Context
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    @Inject
    public FirearmController(
            FirearmService firearmService,
            DtoFunctionFactory factory,
            @SuppressWarnings("CdiInjectionPointsInspection") UriInfo uriInfo) {
        this.firearmService = firearmService;
        this.factory = factory;
        this.uriInfo = uriInfo;
    }

    @Override
    public GetFirearmsResponse getFirearms() {
        return factory.firearmsToResponseFunction().apply(firearmService.findAll());
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
        firearmService.createFirearm(factory.requestToFirearmFunction().apply(id, request));
        // Build Location header explicitly to avoid method-ref UriBuilder issues at runtime
        String location = uriInfo.getBaseUriBuilder()
            .path("api")
            .path("firearms")
            .path(id.toString())
            .build()
            .toString();
        response.setHeader("Location", location);
        throw new WebApplicationException(Response.status(Response.Status.CREATED).build());
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException();
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
