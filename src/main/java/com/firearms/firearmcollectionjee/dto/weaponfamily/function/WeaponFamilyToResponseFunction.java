package com.firearms.firearmcollectionjee.dto.weaponfamily.function;

import com.firearms.firearmcollectionjee.dto.weaponfamily.GetWeaponFamilyResponse;
import com.firearms.firearmcollectionjee.entity.WeaponFamily;

import java.util.function.Function;

public class WeaponFamilyToResponseFunction implements Function<WeaponFamily, GetWeaponFamilyResponse> {
    
    @Override
    public GetWeaponFamilyResponse apply(WeaponFamily weaponFamily) {
        return GetWeaponFamilyResponse.builder()
                .id(weaponFamily.getId())
                .name(weaponFamily.getName())
                .optimalRange(weaponFamily.getOptimalRange())
                .ammoType(weaponFamily.getAmmoType())
                .build();
    }
}
