package com.firearms.firearmcollectionjee.model.weaponfamily.function;

import com.firearms.firearmcollectionjee.entity.WeaponFamily;
import com.firearms.firearmcollectionjee.model.weaponfamily.WeaponFamiliesModel;

import java.util.List;
import java.util.function.Function;

public class WeaponFamiliesToModelFunction implements Function<List<WeaponFamily>, WeaponFamiliesModel> {

    @Override
    public WeaponFamiliesModel apply(List<WeaponFamily> entity) {
        return WeaponFamiliesModel.builder()
                .weaponFamilies(entity.stream()
                        .map(weaponFamily -> WeaponFamiliesModel.WeaponFamily.builder()
                                .id(weaponFamily.getId())
                                .name(weaponFamily.getName())
                                .optimalRange(weaponFamily.getOptimalRange())
                                .ammoType(weaponFamily.getAmmoType())
                                .build())
                        .toList())
                .build();
    }
}
