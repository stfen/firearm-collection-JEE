package com.firearms.firearmcollectionjee.dto.firearm.function;

import com.firearms.firearmcollectionjee.dto.firearm.PatchFirearmRequest;
import com.firearms.firearmcollectionjee.model.Firearm;

import java.util.function.BiFunction;

public class UpdateFirearmWithRequestFunction implements BiFunction<Firearm, PatchFirearmRequest, Firearm> {

    @Override
    public Firearm apply(Firearm firearm, PatchFirearmRequest request) {
        return Firearm.builder()
                .id(firearm.getId())
                .name(request.getName())
                .caliber(firearm.getCaliber())
                .magazineCapacity(request.getMagazineCapacity())
                .weaponFamily(firearm.getWeaponFamily())
                .productionDate(firearm.getProductionDate())
                .user(firearm.getUser())
                .build();
    }
}
