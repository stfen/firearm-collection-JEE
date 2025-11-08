package com.firearms.firearmcollectionjee.entity;

import com.firearms.firearmcollectionjee.entity.enums.AmmoType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Entity class for game characters' professions (classes). Describes name of the profession and skills available on
 * different levels.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "weapon_family")
public class WeaponFamily implements Serializable {
    @Id
    private UUID id;
    private String name;

    @Column(name = "optimal_range")
    private int optimalRange;

    @Column(name = "ammo_type")
    private AmmoType ammoType;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "weaponFamily", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Firearm> firearms;
}
