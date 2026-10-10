package com.firearms.firearmcollectionjee.dto.weaponfamily;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetWeaponFamiliesResponse {

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
    }
    @Singular
    private List<WeaponFamily> weaponFamilies;
}
