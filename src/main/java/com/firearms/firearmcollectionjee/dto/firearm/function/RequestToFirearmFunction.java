package com.firearms.firearmcollectionjee.dto.firearm.function;

import com.firearms.firearmcollectionjee.dto.firearm.PutFirearmRequest;
import com.firearms.firearmcollectionjee.model.Firearm;
import com.firearms.firearmcollectionjee.model.WeaponFamily;

import java.util.UUID;
import java.util.function.BiFunction;

public class RequestToFirearmFunction implements BiFunction<UUID, PutFirearmRequest, Firearm> {
    
    @Override
    public Firearm apply(UUID id, PutFirearmRequest request) {
        return Firearm.builder()
                .id(id)
                .name(request.getName())
                .caliber(request.getCaliber())
                .magazineCapacity(request.getMagazineCapacity())
                .productionDate(request.getProductionDate())
                .weaponFamily(WeaponFamily.builder()
                        .id(request.getWeaponFamilyId())
                        .build())
                .build();
    }
}
