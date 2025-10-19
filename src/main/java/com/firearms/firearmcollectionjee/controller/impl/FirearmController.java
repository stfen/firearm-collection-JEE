package com.firearms.firearmcollectionjee.controller.impl;

import com.firearms.firearmcollectionjee.component.DtoFunctionFactory;
import com.firearms.firearmcollectionjee.controller.api.FirearmControllerInterface;
import com.firearms.firearmcollectionjee.controller.servlet.exception.BadRequestException;
import com.firearms.firearmcollectionjee.controller.servlet.exception.NotFoundException;
import com.firearms.firearmcollectionjee.dto.firearm.GetFirearmResponse;
import com.firearms.firearmcollectionjee.dto.firearm.GetFirearmsResponse;
import com.firearms.firearmcollectionjee.dto.firearm.PatchFirearmRequest;
import com.firearms.firearmcollectionjee.dto.firearm.PutFirearmRequest;
import com.firearms.firearmcollectionjee.model.Firearm;
import com.firearms.firearmcollectionjee.service.FirearmService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@RequestScoped
@NoArgsConstructor(force = true)
public class FirearmController implements FirearmControllerInterface {

    private final FirearmService firearmService;
    private final DtoFunctionFactory factory;

    @Inject
    public FirearmController(FirearmService firearmService, DtoFunctionFactory factory) {
        this.firearmService = firearmService;
        this.factory = factory;
    }

    @Override
    public GetFirearmsResponse getFirearms() {
        return factory.firearmsToResponseFunction().apply(firearmService.findAll());
    }

    @Override
    public GetFirearmsResponse getWeaponFamilyFirearms(UUID weaponFamilyId) {
        List<Firearm> firearms = firearmService.findAllByWeaponFamily(weaponFamilyId)
                .orElseThrow(NotFoundException::new);
        return factory.firearmsToResponseFunction().apply(firearms);
    }

    @Override
    public GetFirearmsResponse getUserFirearms(UUID userId) {
        List<Firearm> firearms = firearmService.findAllByUser(userId)
                .orElseThrow(NotFoundException::new);
        return factory.firearmsToResponseFunction().apply(firearms);
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
