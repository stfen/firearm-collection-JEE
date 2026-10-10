package com.firearms.firearmcollectionjee.dto.weaponfamily;

import com.firearms.firearmcollectionjee.entity.Firearm;
import com.firearms.firearmcollectionjee.entity.enums.AmmoType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode

public class PutWeaponFamilyRequest {
    private String name;
    private int optimalRange;
    private AmmoType ammoType;
}