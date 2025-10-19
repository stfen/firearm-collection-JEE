package com.firearms.firearmcollectionjee.dto.weaponfamily;

import com.firearms.firearmcollectionjee.model.enums.AmmoType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class GetWeaponFamilyResponse {
    private UUID id;
    private String name;
    private int optimalRange;
    private AmmoType ammoType;
}
