package com.firearms.firearmcollectionjee.dto.weaponfamily.function;

import com.firearms.firearmcollectionjee.dto.firearm.PutFirearmRequest;
import com.firearms.firearmcollectionjee.dto.weaponfamily.PutWeaponFamilyRequest;
import com.firearms.firearmcollectionjee.entity.Firearm;
import com.firearms.firearmcollectionjee.entity.WeaponFamily;

import java.util.UUID;
import java.util.function.BiFunction;

public class RequestToWeaponFamilyFunction implements BiFunction<UUID, PutWeaponFamilyRequest, WeaponFamily> {

    @Override
    public WeaponFamily apply(UUID id, PutWeaponFamilyRequest request) {
        return WeaponFamily.builder()
                .id(id)
                .name(request.getName())
                .optimalRange(request.getOptimalRange())
                .ammoType(request.getAmmoType())
                .build();
    }
}

