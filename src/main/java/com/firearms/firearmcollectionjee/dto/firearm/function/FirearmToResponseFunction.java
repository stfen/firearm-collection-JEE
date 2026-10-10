package com.firearms.firearmcollectionjee.dto.firearm.function;

import com.firearms.firearmcollectionjee.dto.firearm.GetFirearmResponse;
import com.firearms.firearmcollectionjee.entity.Firearm;

import java.util.function.Function;

public class FirearmToResponseFunction implements Function<Firearm, GetFirearmResponse> {
    
    @Override
    public GetFirearmResponse apply(Firearm firearm) {
        return GetFirearmResponse.builder()
                .id(firearm.getId())
                .name(firearm.getName())
                .caliber(firearm.getCaliber())
                .magazineCapacity(firearm.getMagazineCapacity())
                .weaponFamily(firearm.getWeaponFamily() != null ? 
                    GetFirearmResponse.WeaponFamily.builder()
                        .id(firearm.getWeaponFamily().getId())
                        .name(firearm.getWeaponFamily().getName())
                        .build() : null)
                .productionDate(firearm.getProductionDate())
                .build();
    }
}
