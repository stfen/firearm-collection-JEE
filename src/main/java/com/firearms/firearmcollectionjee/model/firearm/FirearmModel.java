package com.firearms.firearmcollectionjee.model.firearm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class FirearmModel implements Serializable {
    private UUID id;
    private Long version;
    private String name;
    private double caliber;
    private int magazineCapacity;
    private LocalDate productionDate;
    private LocalDateTime creationDateTime;
    private LocalDateTime lastModificationDateTime;
}
