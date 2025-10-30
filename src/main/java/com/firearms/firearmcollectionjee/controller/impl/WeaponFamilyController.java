package com.firearms.firearmcollectionjee.controller.impl;

import com.firearms.firearmcollectionjee.component.DtoFunctionFactory;
import com.firearms.firearmcollectionjee.controller.api.WeaponFamilyControllerInterface;
import com.firearms.firearmcollectionjee.dto.weaponfamily.GetWeaponFamiliesResponse;
import com.firearms.firearmcollectionjee.dto.weaponfamily.GetWeaponFamilyResponse;
import com.firearms.firearmcollectionjee.entity.WeaponFamily;
import com.firearms.firearmcollectionjee.service.WeaponFamilyService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import lombok.NoArgsConstructor;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.UUID;


@Path("")
public class WeaponFamilyController implements WeaponFamilyControllerInterface {

    private final WeaponFamilyService weaponFamilyService;
    private final DtoFunctionFactory factory;

    @Inject
    public WeaponFamilyController(WeaponFamilyService weaponFamilyService, DtoFunctionFactory factory) {
        this.weaponFamilyService = weaponFamilyService;
        this.factory = factory;
    }

    @Override
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
    public void deleteWeaponFamily(UUID id) {
        weaponFamilyService.findById(id).ifPresentOrElse(
                entity -> weaponFamilyService.deleteWeaponFamily(id),
                () -> {
                    throw new NotFoundException();
                }
        );
    }
}
