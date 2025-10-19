package com.firearms.firearmcollectionjee.dto.firearm;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode

public class PatchFirearmRequest {
    private String name;
    private int magazineCapacity;
}
