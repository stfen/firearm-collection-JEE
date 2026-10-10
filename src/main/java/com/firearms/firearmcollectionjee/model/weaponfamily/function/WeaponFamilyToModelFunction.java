package com.firearms.firearmcollectionjee.model.weaponfamily.function;

import com.firearms.firearmcollectionjee.entity.WeaponFamily;
import com.firearms.firearmcollectionjee.model.weaponfamily.WeaponFamilyModel;

import java.util.function.Function;

/**
 * Converts WeaponFamily entity to WeaponFamilyModel (for JSF views).
 */
public class WeaponFamilyToModelFunction implements Function<WeaponFamily, WeaponFamilyModel> {

    @Override
    public WeaponFamilyModel apply(WeaponFamily wf) {
        if (wf == null) return null;

        return WeaponFamilyModel.builder()
                .id(wf.getId())
                .name(wf.getName())
                .optimalRange(wf.getOptimalRange())
                .ammoType(wf.getAmmoType())
                .build();
    }
}
