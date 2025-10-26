package com.firearms.firearmcollectionjee.controller.impl;

import com.firearms.firearmcollectionjee.component.DtoFunctionFactory;
import com.firearms.firearmcollectionjee.controller.api.WeaponFamilyControllerInterface;
import com.firearms.firearmcollectionjee.dto.weaponfamily.GetWeaponFamiliesResponse;
import com.firearms.firearmcollectionjee.entity.WeaponFamily;
import com.firearms.firearmcollectionjee.service.WeaponFamilyService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import lombok.NoArgsConstructor;

import java.util.List;


@RequestScoped
@NoArgsConstructor(force = true)
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
}
