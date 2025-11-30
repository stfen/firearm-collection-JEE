package com.firearms.firearmcollectionjee.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Entity class for game characters' professions (classes). Describes name of the profession and skills available on
 * different levels.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "firearms")
public class Firearm extends VersionAndAuditDateAuditable implements Serializable {

    @Id
    private UUID id;

    private String name;
    private double caliber;

    @Column(name = "magazine_capacity")
    private int magazineCapacity;

    @ManyToOne
    @JoinColumn(name = "weapon_family")
    private WeaponFamily weaponFamily;

    @Column(name = "production_date")
    private LocalDate productionDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
