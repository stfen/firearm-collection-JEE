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

public class PutFirearmRequest {
    private String name;
    private double caliber;
    private int magazineCapacity;
    private LocalDate productionDate;
    private UUID weaponFamilyId;
}
