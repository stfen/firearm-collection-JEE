package com.firearms.firearmcollectionjee.dto.firearm;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetFirearmResponse {
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
    private UUID id;
    private String name;
    private double caliber;
    private int magazineCapacity;
    private WeaponFamily weaponFamily;
    private LocalDate productionDate;
}
