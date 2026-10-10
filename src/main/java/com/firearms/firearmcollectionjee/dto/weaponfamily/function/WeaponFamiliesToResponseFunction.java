package com.firearms.firearmcollectionjee.dto.weaponfamily.function;

import com.firearms.firearmcollectionjee.dto.weaponfamily.GetWeaponFamiliesResponse;
import com.firearms.firearmcollectionjee.entity.WeaponFamily;

import java.util.List;
import java.util.function.Function;

public class WeaponFamiliesToResponseFunction implements Function<List<WeaponFamily>, GetWeaponFamiliesResponse> {
    
    @Override
    public GetWeaponFamiliesResponse apply(List<WeaponFamily> weaponFamilies) {
        return GetWeaponFamiliesResponse.builder()
                .weaponFamilies(weaponFamilies.stream()
                        .map(weaponFamily -> GetWeaponFamiliesResponse.WeaponFamily.builder()
                                .id(weaponFamily.getId())
                                .name(weaponFamily.getName())
                                .build())
                        .toList())
                .build();
    }
}
