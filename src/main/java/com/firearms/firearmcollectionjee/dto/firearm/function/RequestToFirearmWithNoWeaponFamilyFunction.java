package com.firearms.firearmcollectionjee.dto.firearm.function;

import com.firearms.firearmcollectionjee.dto.firearm.PutFirearmWithNoWeaponFamilyRequest;
import com.firearms.firearmcollectionjee.entity.Firearm;
import com.firearms.firearmcollectionjee.entity.User;
import com.firearms.firearmcollectionjee.entity.WeaponFamily;

import java.util.UUID;

/**
 * Converts PutFirearmWithNoWeaponFamilyRequest to Firearm entity.
 * Takes firearm ID and weaponFamily ID as separate parameters.
 */
public class RequestToFirearmWithNoWeaponFamilyFunction {
    
    /**
     * Convert request DTO to Firearm entity.
     * 
     * @param id the firearm ID
     * @param weaponFamilyId the weapon family ID
     * @param request the request containing firearm properties
     * @return Firearm entity
     */
    public Firearm apply(UUID id, UUID weaponFamilyId, PutFirearmWithNoWeaponFamilyRequest request) {
        return Firearm.builder()
                .id(id)
                .name(request.getName())
                .caliber(request.getCaliber())
                .magazineCapacity(request.getMagazineCapacity())
                .productionDate(request.getProductionDate())
                .user(User.builder()
                        .id(request.getUserId())
                        .build())
                .weaponFamily(WeaponFamily.builder()
                        .id(weaponFamilyId)
                        .build())
                .build();
    }
}
