package com.firearms.firearmcollectionjee.model.weaponfamily;

import com.firearms.firearmcollectionjee.entity.enums.AmmoType;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Wrapper view model containing a list of WeaponFamilyModel instances.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class WeaponFamiliesModel implements Serializable {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @ToString
    @EqualsAndHashCode
    public static class WeaponFamily {
        private UUID id;
        private String name;
        private int optimalRange;
        private AmmoType ammoType;
    }

    @Singular
    private List<WeaponFamily> weaponFamilies;

}
 
